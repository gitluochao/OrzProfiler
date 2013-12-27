package com.zero.orzprofiler.message;

import com.zero.orzprofiler.session.Session;
import com.zero.orzprofiler.swap.ByteBufferFreezers;
import com.zero.orzprofiler.swap.Freezer;
import com.zero.orzprofiler.util.Events;
import com.zero.orzprofiler.util.MemoryMonitor;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: luochao
 * proxy pattern
 * this class can free memory
 * Date: 13-12-27
 * Time: 上午11:18
 */
public class ByteBufferMessageCompactor implements MessageFactory<ByteBuffer>{
    private final MessageFactory<ByteBuffer> MESSAGEFACTORY = new ByteBufferMessageFactory();
    private final ByteBufferFreezers freezers ;

    private AtomicBoolean compacting = new AtomicBoolean(false);
    private Runnable compactTask = new CompactTask();
    private MemoryMonitor monitor;
    private Queue<Message<ByteBuffer>> waiting = new ConcurrentLinkedQueue<Message<ByteBuffer>>();
    public ByteBufferMessageCompactor(MemoryMonitor monitor,final File home,int capacity,int bufferSize) {
        this.monitor = monitor;
        freezers = new ByteBufferFreezers(home,capacity,bufferSize);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
               freezers.dispose();
            }
        }));
    }

    @Override
    public Message<ByteBuffer> createBy(ByteBuffer byteBuffer, Category category, Session session) {
        if(monitor.isShortage() && compacting.compareAndSet(false,true))
            Events.enqueue(compactTask);
        final Message<ByteBuffer> message = MESSAGEFACTORY.createBy(byteBuffer,category,session);
        waiting.add(message);
        return uselessMessageDecorator(message);
    }
    private Message<ByteBuffer> uselessMessageDecorator(Message<ByteBuffer> message){
        return new UerLessMessageDecorator(message);
    }
    private class CompactTask implements Runnable{
        @Override
        public void run() {
           while (true){
               final Message<ByteBuffer> message = waiting.poll();
               if (message == null)
                   break;
               if (message.isExpired() || message.isUseLess())
                   message.dispose();
               else
                   message.freezeBy(freezers.freezer(message.category()));
           }
           compacting.compareAndSet(true,false);
        }
    }
    private Runnable removeCompactTask(final Message<ByteBuffer> message){
        return new Runnable() {
            @Override
            public void run() {
                waiting.remove(message);
            }
        };
    }
    private class UerLessMessageDecorator implements Message<ByteBuffer>{
        private final Message<ByteBuffer> message;

        private UerLessMessageDecorator(Message<ByteBuffer> message) {
            this.message = message;
        }

        @Override
        public Category category() {
            return message.category();
        }

        @Override
        public ByteBuffer content() {
            return message.content();
        }

        @Override
        public boolean isExpired() {
            return message.isExpired();
        }

        @Override
        public boolean isUseLess() {
            return message.isUseLess();
        }

        @Override
        public long created() {
            return message.created();
        }

        @Override
        public Session publisher() {
            return message.publisher();
        }

        @Override
        public void registerReader(Session subscriber) {
            message.registerReader(subscriber);
            if (message.isUseLess())
                Events.enqueue(removeCompactTask(message));
        }

        @Override
        public Set<String> subscribers() {
            return message.subscribers();
        }

        @Override
        public void dispose() {
             message.dispose();
        }

        @Override
        public void freezeBy(Freezer<ByteBuffer> freezer) {
             message.freezeBy(freezer);
        }
    }

}
