package com.zero.orzprofiler.utils;

import java.nio.ByteBuffer;
import static com.zero.orzprofiler.swap.Chunk.Segment.*;
/**
 * Created with IntelliJ IDEA.
 * User: luochao
 * Date: 13-12-19
 * Time: 下午7:06
 * To change this template use File | Settings | File Templates.
 */
public class ByteBufferUtil {

    public static ByteBuffer toBuffer(int size){
        return (ByteBuffer)ByteBuffer.wrap(new byte[DATA_SIZE_LENGTH]).putInt(size).flip();

    }
}
