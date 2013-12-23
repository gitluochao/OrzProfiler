package com.zero.orzprofiler.swap;

import junit.extensions.TestSetup;
import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-20
 * Time: 下午3:56
 */
public class FileChunkTest {
    @Test
    public void testFreeze(){
        String fileName = "D:\\export\\home\\tomcat\\logs\\orzProfiler\\file2.txt";
        File home = new File(fileName);
        int messageSize = 1;
        long capacity = 32 * 2*2*2;
        FileChunk fileChunk = new FileChunk(messageSize,home,capacity);
        String log = "tOrderSyncDirectPayRequestOrderSyncDirectPayRequest OrderSyncDirectPayRequest:"+"["+1+"]"+ System.nanoTime();
        Point<ByteBuffer> point1 = fileChunk.freeze(ByteBuffer.wrap(log.getBytes()));
        Point<ByteBuffer> point2 = fileChunk.freeze(ByteBuffer.wrap(log.getBytes()));

        System.out.println(fileChunk.getSize());
        point1.dispose();
        point2.dispose();
    }

}
