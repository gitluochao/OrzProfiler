package com.zero.orzprofiler.swap;

import com.zero.orzprofiler.utils.ByteBufferUtil;
import org.apache.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static  com.zero.orzprofiler.swap.Chunk.Segment.*;
/**
 * User: luochao
 * Date: 13-12-18
 * Time: 下午4:55
 */
public class BufferImpl implements Buffer{
    private final static Logger logger = Logger.getLogger(BufferImpl.class);
    private static final int DEFAULT_CAPACITY = 32;
    private final Chunk.Segment segment;
    //message num
    private final int capacity;
    private final int maxByteBufferSize;
    private final ReferenceFactory referenceFactory;
    private final List<ByteBuffer> byteBuffersForWrite = new ArrayList<ByteBuffer>();

    private final AtomicLong size = new AtomicLong(0);
    private boolean freezed = false;
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
    public synchronized void freeze() {
        if(freezed) return;
        try {
            segment.write(byteBuffersForWrite.toArray(new ByteBuffer[0]),size.get());
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            freezed = true;
            byteBuffersForWrite.clear();
        }

    }

    @Override
    public synchronized boolean hasRemainFor(ByteBuffer byteBuffer) {
        return byteBuffer.remaining() + DATA_SIZE_LENGTH + size.get() < capacity * (maxByteBufferSize + DATA_SIZE_LENGTH);
    }

    @Override
    public Point<ByteBuffer> write(ByteBuffer byteBuffer) {
        if(!hasRemainFor(byteBuffer))
            throw new IllegalStateException(this+"has not remaining for buffer");
        if(freezed){
            throw new IllegalStateException(this+"buffer have been freezed");
        }
        final long position = size.addAndGet(DATA_SIZE_LENGTH);
        byteBuffersForWrite.add(ByteBufferUtil.toBuffer(byteBuffer.remaining()));
        final ByteBuffer duplicate = byteBuffer.duplicate();
        byteBuffersForWrite.add(duplicate);

        size.addAndGet(byteBuffer.remaining());
        duplicate.mark();
        return null;
    }
    private final static ReferenceFactory DEFAULT_REFERENCE_FACTORY = new ReferenceFactory<byte[]>() {
        @Override
        public Reference<byte[]> createBy(byte[] data) {
        return new WeakReference<byte[]>(data);
        }
    };
    private interface ReferenceFactory<T>{
        Reference<T> createBy(T data);
    }
}
