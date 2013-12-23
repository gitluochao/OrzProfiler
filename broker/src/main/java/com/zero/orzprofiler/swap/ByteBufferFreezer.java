package com.zero.orzprofiler.swap;

import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import java.io.File;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: luochao
 * Date: 13-12-23
 * Time: 下午7:14
 */
public class ByteBufferFreezer implements Freezer<ByteBuffer> {
    private final long capacity;
    private final long bufferSize;
    private final String home;
    private  Chunk chunk;
    private int seq = 0;

    public ByteBufferFreezer(long capacity, long bufferSize, String home) {
        this.capacity = capacity;
        this.bufferSize = bufferSize;
        this.home = home;
        chunk = newChunk();
    }
    private Chunk newChunk(){
       return null;
    }
    @Override
    public Point<ByteBuffer> freeze(ByteBuffer message) {
        return null;
    }

    @Override
    public void dispose() {
    }
    private static final class Chunk{
        private final static Logger log = Logger.getLogger(Chunk.class);
        private boolean fix = false;

        private static final int DATA_SIZE_LENGTH = 4;
        private long position;
        private final int capacity;
        private final File filePath;
        private FileChannel fileChannel;
        private ByteBuffer byteBuffer;
        private Queue<ReplaceBufferPoint> pending = new LinkedBlockingDeque<ReplaceBufferPoint>();

        public Chunk(File filePath , int capacity,int bufferSize ) {
            this.capacity = capacity;
            this.filePath = filePath;
            byteBuffer = ByteBuffer.allocate(bufferSize);
            log.info("chunk created");
        }
        public Point<ByteBuffer> freeze(ByteBuffer byteBuffer){
               return null;
        }
        private boolean hasRemainFor(int  size){
            return size+DATA_SIZE_LENGTH + position <= byteBuffer.capacity();
        }
        private final static class ByteBufferPoint implements Point<ByteBuffer>{
            private AtomicReference<ByteBuffer> reference;
            private int position;
            private int length;

            private ByteBufferPoint(ByteBuffer buffer, int position, int length) {
                reference = new AtomicReference<ByteBuffer>(buffer);
                this.position = position;
                this.length = length;
            }

            @Override
            public ByteBuffer get() {
                return (ByteBuffer)reference.get().duplicate().position(position).limit(position+length);
            }

            @Override
            public void dispose() {
               reference.getAndSet(null);
            }
        }
        private final static class ReplaceBufferPoint implements Point<ByteBuffer>{
            private AtomicReference<Point<ByteBuffer>> reference;
            private int position;
            private int length;
            private ReplaceBufferPoint(Point<ByteBuffer> point, int position, int length) {
                reference = new AtomicReference<Point<ByteBuffer>>(point);
                this.position = position;
                this.length = length;
            }

            @Override
            public ByteBuffer get() {
                return reference.get().get();
            }

            @Override
            public void dispose() {
               if(reference.get() != null)
                   reference.getAndSet(null).dispose();
            }
            public void replace(Point<ByteBuffer> point){
                final  Point<ByteBuffer>  prePoint = reference.getAndSet(point);
                if (prePoint == null){
                    prePoint.dispose();
                }
            }
        }
    }
}
