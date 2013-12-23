package com.zero.orzprofiler.util;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-20
 * Time: 上午10:41
 */
public class BufferTest {
    @Test
    public void testTobuffer(){
        int i = 20;
        ByteBuffer buffer = ByteBufferUtil.toBuffer(i);
        System.out.print(buffer.toString());
    }
}
