package com.zero.orzprofiler.swap;

import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import java.io.*;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: luochao
 * Date: 13-12-23
 * Time: 下午7:14
 */
public class ByteBufferFreezer implements Freezer<ByteBuffer> {
    private final int capacity;
    private final int bufferSize;
    private final File home;
    private  Chunk chunk;
    private int seq = 0;

    public ByteBufferFreezer(File home , int capacity, int bufferSize) {
        this.capacity = capacity;
        this.bufferSize = bufferSize;
        this.home = home;
        chunk = newChunk();
        if (!home.exists())
            home.mkdir();
    }
    private Chunk newChunk(){
       return new Chunk(new File(home,seq()+""),capacity,bufferSize);
    }
    @Override
    public Point<ByteBuffer> freeze(ByteBuffer message) {
        return null;
    }
    public int seq(){
        return seq == Integer.MAX_VALUE ? (seq = 0) : seq++;
    }
    @Override
    public void dispose() {
    }
    private static final class Chunk{
        private final static Logger log = Logger.getLogger(Chunk.class);
        private boolean fix = false;

        private static final int DATA_SIZE_LENGTH = 4;
        private int position;
        private final int capacity;
        private final File filePath;
        private ByteBuffer byteBuffer;
        private Queue<ReplaceBufferPoint> pending = new LinkedBlockingDeque<ReplaceBufferPoint>();

        public Chunk(File filePath , int capacity,int bufferSize ) {
            this.capacity = capacity;
            this.filePath = filePath;
            byteBuffer = ByteBuffer.allocate(bufferSize);
            log.info("chunk created");
        }
        public Point<ByteBuffer> freeze(ByteBuffer byteBuffer){
            if(fix) throw new IllegalStateException("Can not freeze  a fix chunk");
            int length = byteBuffer.remaining();
            if(!hasRemainFor(length)){
                flush();
                byteBuffer.clear();
            }
            byteBuffer.putInt(length);
            ByteBufferPoint byteBufferPoint = new ByteBufferPoint(byteBuffer,byteBuffer.position(),length);
            byteBuffer.put(byteBuffer);
            ReplaceBufferPoint replaceBufferPoint = new ReplaceBufferPoint(byteBufferPoint,position,length);
            //wait flush data to disk file
            pending.add(replaceBufferPoint);
            forward(length);
            return byteBufferPoint;
        }
        private void flush(){
            if(pending.isEmpty()) return;
            try{
                doflush();
                replacePending();
            }catch (Exception e){
                throw  new RuntimeException(e);
            }
        }
        private void doflush() throws FileNotFoundException,IOException{
            FileOutputStream fout = new FileOutputStream(filePath);
            try {
                FileChannel fileChannel = fout.getChannel();
                fileChannel.write(byteBuffer);
                fileChannel.close();
            }finally {
                fout.close();
            }
        }
        private void replacePending() throws IOException{
            if(pending.isEmpty()) return;
            Slice slice = null;
            while (!pending.isEmpty()){
                if (slice == null)
                    slice = new Slice(filePath,pending.size(),fix);
                ReplaceBufferPoint replaceBufferPoint = pending.remove();
                replaceBufferPoint.replace(slice.slice(replaceBufferPoint.position,replaceBufferPoint.length));
            }
        }

        private void forward(int length){
            position += length+DATA_SIZE_LENGTH;
        }
        private boolean hasRemainFor(int  size){
            return size+DATA_SIZE_LENGTH + position <= byteBuffer.capacity();
        }
        private boolean hasRemainFor(ByteBuffer byteBuffer){
            return byteBuffer.remaining() + DATA_SIZE_LENGTH + position <= byteBuffer.capacity();
        }
        private final static class Slice{
            private final File  filePath;
            private final FileChannel fileChannel;
            private final AtomicInteger reference;
            private final boolean last;
            public Slice(File filePath,int size,boolean last) throws IOException{
                this.filePath = filePath;
                final FileInputStream fileInputStream = new FileInputStream(filePath);
                fileChannel = fileInputStream.getChannel();
                reference = new AtomicInteger(size);
                this.last = last;
            }
            private Point<ByteBuffer> slice(final int position,final int length){
                return new Point<ByteBuffer>() {
                    @Override
                    public ByteBuffer get() {
                        final ByteBuffer buffer = ByteBuffer.allocate(length);
                        try {
                            fileChannel.read(buffer,position + DATA_SIZE_LENGTH);
                        }catch (final Exception e){
                            log.error(e);
                        }
                        return (ByteBuffer)buffer.flip();
                    }

                    @Override
                    public void dispose() {
                        if(reference.decrementAndGet() == 0){
                            try {
                                fileChannel.close();
                            }catch (Exception e){
                                log.error("Can't close file channel "+ fileChannel);
                            }
                            if (last){
                                if(filePath.delete())
                                    log.info("delete"+filePath.getPath());
                                else
                                    log.warn("delete failed " + filePath.getPath());
                            }
                        }
                    }
                };
            }


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
