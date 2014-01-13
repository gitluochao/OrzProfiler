package com.zero.orzprofiler.profiler;

import com.zero.orzprofiler.message.AppendMessage;
import com.zero.orzprofiler.message.Message;
import com.zero.orzprofiler.zookeeper.Disposeable;
import com.zero.orzprofiler.zookeeper.DisposeableRepository;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by luochao on 14-1-11.
 */
public class ConcurrentChain<Content> implements Chain<Content>{
    private final static Logger log = Logger.getLogger(ConcurrentChain.class);
    private final String category;
    private AtomicInteger size = new AtomicInteger();
    private final static AtomicReferenceFieldUpdater<ConcurrentChain,Node> TAIL_UPDATE = AtomicReferenceFieldUpdater.newUpdater(ConcurrentChain.class,Node.class,"tail");
    private final static AtomicReferenceFieldUpdater<ConcurrentChain,Node> HEAD_UPDATE = AtomicReferenceFieldUpdater.newUpdater(ConcurrentChain.class,Node.class,"head");
    private volatile Node<Content> tail;
    private volatile Node<Content> head;
    private AtomicBoolean dispose = new AtomicBoolean(false);
    private final Cursors cursors = new Cursors();
    public ConcurrentChain(String category) {
        this.category = category;
        head = tail = new Node<Content>(null,null);
        log.info(String.format("ConcurrentChain %s create {}",category));
    }
    private boolean casHead(final Node<Content> expect,final Node<Content> update){
        return HEAD_UPDATE.compareAndSet(this,expect,update);
    }
    private boolean  casTail(final Node<Content> expect,final Node<Content> update){
        return TAIL_UPDATE.compareAndSet(this,expect,update);
    }

    @Override
    public Cursor<Message<Content>> cursorOf(Object key) {
        try{
            return cursors.getOrCreateIfNoExist(key);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public void post(Message<Content> message)  {
        if (dispose.get())
            throw new IllegalStateException("this Chain has been disposed");
        checkNotNull(message);
        final Node<Content> node = new Node<Content>(null,message);
        while (true){
            Node<Content> t = tail;
            Node<Content> n = t.next;
            if(t == tail){//tail unchange
                if (n == null){//tail is last node
                   casTail(t,node);
                    size.incrementAndGet();
                    return;
                }else {
                    casTail(t,n);
                }
            }
        }

    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public int trim() {
        checkHead();
        int trim = 0;
        while (true){
            final Node<Content> h = head,t = tail,n = h.next;
            final Message<Content> message = h.message;
            if (h == head){//check head unchanged
                if (h == t){
                    if (n == null)
                        break;
                    casTail(t,n);
                }else {
                    if (checktrim(message)){
                        message.dispose();
                        if (casHead(h,n)) trim++;
                        continue;
                    }else {
                        break;
                    }
                }
            }
        }
        size.addAndGet(-trim);
        log.debug(String.format("%s trim{} message",category));
        return trim;
    }

    @Override
    public void dispose() {
        if(dispose.compareAndSet(false,true)){
            return;
        }
        while (true){
            final Node<Content> h = head,t = tail,n = head.next;
            final Message<Content> message = h.message;
            if (h == head){//head is unchanged
                if(h == t){
                    if (n == null) break;
                    casTail(t,n);
                }
            }else {
                message.dispose();
                if (casHead(h,n)) size.decrementAndGet();
                continue;
            }
        }

    }
    private void checkHead(){
        Node<Content> h = head;
        Node<Content> n = h.next;
        if(head.message == null && casHead(h,h)){
            size.decrementAndGet();
        }
    }
    private void checkNotNull(Message<Content> message){
        if (message == null)
            throw new NullPointerException("message can't null");
    }
    private static boolean checktrim(final Message<?> message){
        return message.isExpired() || message.isUseLess();
    }
    @Override
    public void dumpto(AppendMessage<Content> appendMessage) {

    }
    private final class Cursors extends DisposeableRepository<Object,InnerCursor>{
         public Cursors(){
             super(new Factory<Object, InnerCursor>() {
                 @Override
                 public InnerCursor newInstance(Object key) {
                     return new InnerCursor();
                 }
             });
         }
    }
    private final class InnerCursor implements Cursor<Message<Content>>,Disposeable{
        private Node<Content> begin;
        public InnerCursor(){
            this.begin = head;
        }

        @Override
        public void dispose() {
            this.begin = null;
        }

        @Override
        public Message<Content> next() {
            Node<Content> next = begin.next;
            if (next == null){
                return null;
            }
            begin = next;
            return begin.message;
        }
    }
    private static class Node<Content>{
        private Message<Content> message;
        volatile Node<Content> next;
        private AtomicReferenceFieldUpdater<Node,Node> NEXT_UPDATE = AtomicReferenceFieldUpdater.newUpdater(Node.class,Node.class,"next");
        private Node(final Node<Content> next,final Message<Content> message){
            this.message = message;
            this.next = next;
        }
        public synchronized void casNext(final Node expext,final Node update){
            NEXT_UPDATE.compareAndSet(this,expext,update);
        }
    }

}
