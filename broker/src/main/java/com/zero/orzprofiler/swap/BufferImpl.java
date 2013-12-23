package com.zero.orzprofiler.swap;

import com.zero.orzprofiler.util.ByteBufferUtil;
import org.apache.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import static com.zero.orzprofiler.swap.Chunk.Segment.*;
/**
 * User: luochao
 * Date: 13-12-18
 * Time: 下午4:55
 */
public class BufferImpl implements Buffer{
    private final static Logger logger = Logger.getLogger(BufferImpl.class);
    private static final int DEFAULT_CAPACITY = 32;
    private final Chunk.Segment segment;
    private final int capacity;
    private final int maxByteBufferSize;
    private final ReferenceFactory referenceFactory;
    private final List<ByteBuffer> byteBuffersForWrite = new ArrayList<ByteBuffer>();

    private final AtomicInteger size = new AtomicInteger(0);
    private boolean freezed;
    private Reference<byte[]> cache;

    public BufferImpl(Chunk.Segment segment, int capacity, int maxByteBufferSize, ReferenceFactory referenceFactory) {
        this.segment = segment;
        this.capacity = capacity;
        this.maxByteBufferSize = maxByteBufferSize;
        this.referenceFactory = referenceFactory;
    }

    public BufferImpl(Chunk.Segment segment, int maxByteBufferSize) {
        this(segment,DEFAULT_CAPACITY,maxByteBufferSize,DEFAULT_REFERENCE_FACTORY);
    }

    @Override
    public void freeze() {
        if(freezed)
            return;
        try{
            segment.write(byteBuffersForWrite.toArray(new ByteBuffer[0]),size.get());
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            byteBuffersForWrite.clear();
            freezed = true;
        }
    }

    @Override
    public boolean hasRemainFor(ByteBuffer byteBuffer) {
        if(freezed) throw new IllegalStateException("Buffer has been freeze");
        return byteBuffer.remaining() + DATA_SIZE_LENGTH + size.get() <= capacity * (maxByteBufferSize+DATA_SIZE_LENGTH);
    }

    @Override
    public Point<ByteBuffer> write(ByteBuffer byteBuffer) {
        if(!hasRemainFor(byteBuffer))
            throw new IllegalStateException("has not space for byteBuffer");
        if(freezed)
            throw new IllegalStateException("this buffer has being freezen");
        final int position = size.addAndGet(DATA_SIZE_LENGTH);
        size.addAndGet(byteBuffer.remaining());
        byteBuffersForWrite.add(ByteBufferUtil.toBuffer(byteBuffer.remaining()));
        final ByteBuffer duplicate = byteBuffer.duplicate();
        duplicate.mark();
        byteBuffersForWrite.add(byteBuffer);
        return point(weakReference(duplicate),newSlice(position,byteBuffer.remaining()));
    }

    private Point<ByteBuffer> point(WeakReference<ByteBuffer> reference,Slice slice){
        return new ByteBufferPoint(reference,slice);
    }
    private WeakReference<ByteBuffer> weakReference(ByteBuffer byteBuffer){
        return new  WeakReference<ByteBuffer>(byteBuffer);
    }
    private synchronized ByteBuffer slice(int position,int length){
        byte[] ret = null;
        if(cache.get() != null)
            ret = cache.get();
        if(ret == null){
            ret = new byte[size.get()];
            try{
                segment.read(ret);
                cache = newCacheOf(ret);
            }catch (final Exception e){
                throw new RuntimeException(e);
            }
        }
        return ByteBuffer.wrap(ret,position,length);
    }
    private Slice newSlice(final int position,final int length){
        return new Slice() {
            @Override
            public ByteBuffer get() {
                return slice(position,length);
            }

            @Override
            public void remove() {
                segment.reduce(length);
            }
        };
    }
    private Reference<byte[]> newCacheOf(byte[] array){
        return referenceFactory.createBy(array);
    }
    private final static ReferenceFactory DEFAULT_REFERENCE_FACTORY = new ReferenceFactory<byte[]>() {
        @Override
        public Reference<byte[]> createBy(byte[] data) {
            return new SoftReference<byte[]>(data);
        }
    };
    private interface ReferenceFactory<T>{
        Reference<T> createBy(T data);
    }
}
