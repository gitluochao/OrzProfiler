package com.zero.orzprofiler.profiler.cache;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.*;

/**
 * User: luochao
 * Date: 13-11-9
 * Time: 涓嫔崃12:19
 */
public class CacheManager<T,V> {
    private final  Map<T,Future<V>> cache = new ConcurrentHashMap<T, Future<V>>();
    private final static Executor executor = Executors.newFixedThreadPool(100);
    public V get(T args){
        Future<V> f = cache.get(args);
        if(f == null){
            LoadDbTask<T,V> loadDbTask = new LoadDbTask<T, V>(args);
            FutureTask<V> task = new FutureTask<V>(loadDbTask);
            f = task;
            cache.put(args,task);
            task.run();
        }
        try{
            return f.get();
        }catch (ExecutionException e){

        }catch (InterruptedException e){
            Thread.interrupted();
        }
        return null;
    }
    @Test
    public void testExcutor(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println("asdfffff");
            }
        };
        executor.execute(run);


    }
}
