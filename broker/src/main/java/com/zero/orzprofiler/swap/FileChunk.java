package com.zero.orzprofiler.swap;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicLong;
import static com.zero.orzprofiler.swap.Chunk.Segment.*;

/**
 * User: luochao
 * Date: 13-12-18
 * Time: 下午3:21
 */
public class FileChunk implements Chunk {
    private final static Logger logger = Logger.getLogger(FileChunk.class);
    private final int maxMessageSize;
    private final File filePath;
    private final long capacity;
    private final AtomicLong size = new AtomicLong();
    private final FileChannel fileChannel;
    private Buffer buffer = new NullBuffer();

    public FileChunk(int maxMessageSize, File filePath, long capacity) {
        this.maxMessageSize = maxMessageSize;
        this.filePath = filePath;
        this.capacity = capacity;
        try{
            fileChannel = new RandomAccessFile(filePath,"rwd").getChannel();
        }catch (final Exception e){
            throw new  RuntimeException(e);
        }
        logger.info(String.format("{%s}filechunk created",this.toString()));
    }

    @Override
    public void close() {
        try{
            buffer.freeze();
            fileChannel.close();
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public boolean hasRemainFor(ByteBuffer byteBuffer) {
        return size.get()+DATA_SIZE_LENGTH+byteBuffer.remaining() <= capacity;
    }

    @Override
    public Point<ByteBuffer> freeze(ByteBuffer byteBuffer) {
        if(!hasRemainFor(byteBuffer)){
           throw new IllegalArgumentException(this+"has not remain space the buffer");
        }
        //文件写入位置
        final long postion = size.getAndAdd(DATA_SIZE_LENGTH+byteBuffer.remaining());
        if(!buffer.hasRemainFor(byteBuffer)){
            buffer.freeze();

        }
        return buffer.write(byteBuffer);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("filepath:").
                append(filePath.getPath())
                .append("file capacity:")
                .append(capacity)
                .append("max message size:")
                .append(maxMessageSize)
                .append("readOnly:")
                .append(fileChannel.isOpen());
        return builder.toString();
    }
    private Segment segment(){
        return new Segment() {
            @Override
            public void read(byte[] buffer) throws IOException {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void write(ByteBuffer[] byteBuffers, long size) throws IOException {
                long write = 0;
                while (write < size){
                   write += fileChannel.write(byteBuffers);
                }
            }
        };
    }

    private class NullBuffer implements Buffer{
        @Override
        public void freeze() {
        }

        @Override
        public boolean hasRemainFor(ByteBuffer byteBuffer) {
            return false;
        }

        @Override
        public Point<ByteBuffer> write(ByteBuffer byteBuffer) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
