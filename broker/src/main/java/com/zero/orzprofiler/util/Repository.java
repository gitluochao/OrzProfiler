package com.zero.orzprofiler.util;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * User: luochao
 * Date: 13-12-25
 * Time: 下午6:11
 */
public abstract class Repository<K,V> implements  IRepository<K,V>{
    private Factory<K,V> factory;
    public final ConcurrentHashMap<K,FutureTask<V>> map = new ConcurrentHashMap<K,FutureTask<V>>();

    protected Repository(Factory<K, V> factory) {
        this.factory = factory;
    }

    @Override
    public V uncheckedGetOrCreateIfNoExist(final K key) throws InterruptedException, ExecutionException{
        FutureTask<V> newCreater = newCreater(key);
        newCreater.run();
        return newCreater.get();
    }

    @Override
    public V getOrCreateIfNoExist(final K key) throws InterruptedException, ExecutionException{
        final FutureTask<V> future = map.get(key);
        if(future != null)
            return future.get();
        FutureTask<V> newCreater = newCreater(key);
        final FutureTask<V> oldCreater = map.putIfAbsent(key,newCreater);
        if (oldCreater == null){
            newCreater.run();
            return newCreater.get();
        }
        oldCreater.run();
        return oldCreater.get();
    }
    private  FutureTask<V> newCreater(final K key){
        return new FutureTask<V>(new Callable<V>() {
            @Override
            public V call() throws Exception {
              return factory.newInstance(key);
            }
        });
    }
    public interface Factory<K,V>{
        V newInstance(final K key);
    }
}
