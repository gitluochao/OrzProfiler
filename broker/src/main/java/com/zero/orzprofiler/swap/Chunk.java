package com.zero.orzprofiler.swap;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-18
 * Time: 下午3:00
 */
public interface Chunk {
    /**
     * Close {@link Chunk} if not space for buffer
     */
    void close();

    /**
     * if can not be write after freeze
     * @param byteBuffer
     * @return
     */
    Point<ByteBuffer> freeze(ByteBuffer byteBuffer);

    /**
     * return true if {@link Chunk} have space for {@ByteBuffer}
     * @param byteBuffer
     * @return
     */
    boolean hasRemainFor(ByteBuffer byteBuffer);

    /**
     * <pre> : [dataSize][data]></pre>
     */
    interface Segment{

        void read(byte[] buffer) throws IOException;

        void write(ByteBuffer[] byteBuffers,long size) throws IOException;

        void reduce(final int length);
        /**
         * record the length of the segment
         */
        int DATA_SIZE_LENGTH = 4;
    }
}
