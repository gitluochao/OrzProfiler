package com.zero.orzprofiler.swap;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.zero.orzprofiler.message.Category;
import com.zero.orzprofiler.swap.ByteBufferFreezers;
import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Set;

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
        ByteBufferFreezers freezers = new ByteBufferFreezers(fileName,capacity,messageSize);
        Category category = new Category() {
            @Override
            public boolean isInvalidSubscriber(String key) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isMessageExpireAfter(long created) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isMessageUselessReadBy(Set<String> subscribers) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String name() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        Freezer<ByteBuffer> byteBufferFreezer = freezers.freezer(category);
        String log = "tOrderSyncDirectPayRequestOrderSyncDirectPayRequest OrderSyncDirectPayRequest:"+"["+1+"]"+ System.nanoTime();
        ByteBuffer buffer = ByteBuffer.wrap(log.getBytes());
        byteBufferFreezer.freeze(buffer);
        byteBufferFreezer.freeze((ByteBuffer)buffer.flip());
        byteBufferFreezer.freeze((ByteBuffer)buffer.flip());
        byteBufferFreezer.freeze((ByteBuffer)buffer.flip());
    }
}
