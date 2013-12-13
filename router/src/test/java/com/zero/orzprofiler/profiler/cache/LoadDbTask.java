package com.zero.orzprofiler.profiler.cache;

import java.util.concurrent.Callable;

/**
 * User: luochao
 * Date: 13-11-9
 * Time: 涓嫔崃12:21
 */
public class LoadDbTask<T,V> implements Callable<V> {
    private final T name;

    public LoadDbTask(T name) {
        this.name = name;
    }

    @Override
    public V call()  {
        try{
            System.out.println("load cache");
            return null;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
