package com.zero.orzprofiler.swap;

import org.apache.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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
    private final List<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>();

    private final int size = 0;
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasRemainFor(ByteBuffer byteBuffer) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Point<ByteBuffer> write(ByteBuffer byteBuffer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
