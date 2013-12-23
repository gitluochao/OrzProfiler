package com.zero.orzprofiler.util;

import java.nio.ByteBuffer;
import static com.zero.orzprofiler.swap.Chunk.Segment.*;

/**
 * User: luochao
 * Date: 13-12-20
 * Time: 上午10:36
 */
public class ByteBufferUtil {
    public static ByteBuffer toBuffer(int size){
        return (ByteBuffer)ByteBuffer.wrap(new byte[DATA_SIZE_LENGTH]).putInt(size).flip();
    }
}
