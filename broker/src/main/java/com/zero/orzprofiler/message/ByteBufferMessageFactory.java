package com.zero.orzprofiler.message;

import com.zero.orzprofiler.session.Attribute;
import com.zero.orzprofiler.session.Session;
import com.zero.orzprofiler.swap.Freezer;
import com.zero.orzprofiler.swap.Point;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午2:54
 */
public class ByteBufferMessageFactory implements MessageFactory<ByteBuffer>{
    @Override
    public Message<ByteBuffer> createBy(ByteBuffer byteBuffer, Category category, Session session) {
        return new ByteBufferMessage(byteBuffer,category,session);
    }
    private final static class ByteBufferMessage implements Message<ByteBuffer>{
        private final static Logger log = Logger.getLogger(ByteBufferMessage.class);
        private final Category category;
        private final Session publisher;
        private ByteBuffer content;
        private Set<String> subscribers;
        private final long created;
        private Point<ByteBuffer> point;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private boolean dispose = false;

        private ByteBufferMessage( ByteBuffer content , Category category, Session publisher) {
            this.category = category;
            this.publisher = publisher;
            this.content = content;
            this.created = System.nanoTime();
            subscribers = Collections.synchronizedSet(new HashSet<String>());
        }

        @Override
        public Category category() {
            return category;
        }

        @Override
        public ByteBuffer content() {
            lock.readLock().lock();
            try{
                if(dispose)
                    throw new IllegalStateException("this message has been dispose");
                if(isExpired())
                    throw new IllegalStateException("this message has been expire");
                if (isUseLess())
                    throw new IllegalStateException("this message has been useless");
                if(content != null)
                    return content.duplicate();
                if (point != null)
                    return point.get();
                throw  new IllegalStateException("this message may be not init");
            }finally {
                lock.readLock().unlock();
            }

        }

        @Override
        public boolean isExpired() {
            return category.isMessageExpireAfter(created);
        }

        @Override
        public boolean isUseLess() {
            return category.isMessageUselessReadBy(subscribers);
        }

        @Override
        public long created() {
            return created;
        }

        @Override
        public Session publisher() {
            return publisher;
        }

        @Override
        public void registerReader(Session subscriber) {
            String sub = subscriber.stringAttribute(Attribute.subscriber);
            log.debug("subscriber by " + sub);
            subscribers.add(sub);
        }

        @Override
        public Set<String> subscribers() {
            return Collections.unmodifiableSet(subscribers);
        }

        @Override
        public void dispose() {
           lock.writeLock().lock();
           try{
               if (dispose)
                  return;
               content = null;
               if(point != null){
                   point.dispose();
                   point = null;
               }
               dispose = true;
           }finally {
               lock.writeLock().unlock();
           }
        }

        @Override
        public void freezeBy(Freezer<ByteBuffer> freezer) {
            lock.writeLock().lock();
            try{
                if (dispose || content== null)
                    return;
                point = freezer.freeze(content);
            }finally {
                lock.writeLock().unlock();
            }

        }
    }
}
