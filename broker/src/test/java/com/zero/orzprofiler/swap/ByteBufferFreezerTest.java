package com.zero.orzprofiler.swap;

import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 上午10:26
 */
public class ByteBufferFreezerTest {
    @Test
    public void testFreeze(){
        String fileName = "D:\\export\\home\\tomcat\\logs\\orzProfiler";
        int messageSize = 32*2*2;
        int capacity = 32 * 2*2*2;
        ByteBufferFreezer byteBufferFreezer = new ByteBufferFreezer(new File(fileName),capacity,messageSize);
        String log = "tOrderSyncDirectPayRequestOrderSyncDirectPayRequest OrderSyncDirectPayRequest:"+"["+1+"]"+ System.nanoTime();
        ByteBuffer buffer = ByteBuffer.wrap(log.getBytes());
        byteBufferFreezer.freeze(buffer);
        byteBufferFreezer.freeze((ByteBuffer)buffer.flip());
        byteBufferFreezer.freeze((ByteBuffer)buffer.flip());
        byteBufferFreezer.freeze((ByteBuffer)buffer.flip());
    }
}
