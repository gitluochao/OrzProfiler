package com.zero.orzprofiler.swap;

import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-18
 * Time: 下午3:14
 */
public interface Buffer {
    /**
     * freeze the buffer
     */
    void freeze();

    /**
     * return true if have space for {@link ByteBuffer}
     * @param byteBuffer
     * @return
     */
    boolean hasRemainFor(ByteBuffer byteBuffer);

    /**
     * write {@link ByteBuffer} into {@Buffer}
     * @param byteBuffer
     * @return
     */
    Point<ByteBuffer> write(ByteBuffer byteBuffer);

    /**
     * {@link slice}
     */
    interface slice{
        /**
         * return {@Buffer} range  position to limit
         * @return
         */
        ByteBuffer get();

        void remove();
    }




}
