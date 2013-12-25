package com.zero.orzprofiler.swap;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * User: luochao
 * Date: 13-12-25
 * Time: 下午2:31
 */
public class NByteBufferFreezer implements Freezer<ByteBuffer> {
    private final File home;
    private final int maxMessageSize;
    private final static int DEFAULT_FILEZ_SIZE = (1<<20)*32;
    private final int fileSize;
    private int seq = 0;
    public NByteBufferFreezer(File home, int maxMessageSize, int fileSize) {
        this.home = home;
        this.maxMessageSize = maxMessageSize;
        this.fileSize = fileSize;
        if (!home.exists())
            home.mkdir();
    }

    private Chunk chunk = new NullChunk();
    @Override
    public Point<ByteBuffer> freeze(ByteBuffer message) {
        if(!chunk.hasRemainFor(message)){
            chunk.close();
            chunk = newChunk(seq());
        }
        return chunk.freeze(message);
    }
    private Chunk newChunk(int seq){
        return new FileChunk(path(seq),fileSize,maxMessageSize);
    }
    private File path(int seq){
        return new File(home,seq+"");
    }
    private int seq(){
        return seq == Integer.MAX_VALUE ? (seq = 0) : seq++;
    }
    @Override
    public void dispose() {
        chunk.close();
    }
    private class NullChunk implements Chunk{
        @Override
        public void close() {
            return;
        }

        @Override
        public Point<ByteBuffer> freeze(ByteBuffer byteBuffer) {
            throw new UnsupportedOperationException("Can't not operation on null chunk");
        }

        @Override
        public boolean hasRemainFor(ByteBuffer byteBuffer) {
            return false;
        }
    }
}
