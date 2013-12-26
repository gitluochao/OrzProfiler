package com.zero.orzprofiler.zookeeper;

import com.zero.orzprofiler.util.Repository;

import java.util.concurrent.FutureTask;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 上午9:49
 */
public class DisposeableRepository<K,V extends Disposeable> extends Repository<K,V> implements Disposeable{
    public DisposeableRepository(Factory<K, V> factory) {
        super(factory);
    }

    @Override
    public void dispose() {
        boolean interrupted = false;
        for (FutureTask<V> futureTask : map.values()){
            try{
                futureTask.get().dispose();
            }catch (Exception e){
                interrupted = true;
            }finally {
                continue;
            }
        }
        map.clear();
        if (interrupted)
            Thread.currentThread().interrupt();
    }
}
