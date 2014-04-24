package com.zero.orzprofiler.swap;

import com.zero.orzprofiler.message.Category;
import com.zero.orzprofiler.zookeeper.DisposeableRepository;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 上午9:58
 */
public class ByteBufferFreezers extends DisposeableRepository<Category,Freezer<ByteBuffer>> {
    public ByteBufferFreezers(final String  home,final int chunkCapacity , final int chunkbuffer) {
        super(new Factory<Category, Freezer<ByteBuffer>>() {
            @Override
            public Freezer<ByteBuffer> newInstance(Category category) {
                return new ByteBufferFreezer(new File(home,category.name()),chunkCapacity,chunkbuffer);
            }
        });
    }
    public Freezer<ByteBuffer> freezer(Category category){
        try {
            return getOrCreateIfNoExist(category);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
