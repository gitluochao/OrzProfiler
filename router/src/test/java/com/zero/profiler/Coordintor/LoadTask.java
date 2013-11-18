package com.zero.profiler.Coordintor;

import java.util.concurrent.Callable;

/**
 * User: luochao
 * Date: 13-11-8
 * Time: 涓嫔崃7:03
 */
public class LoadTask<T> implements Callable<T> {
    private final T taskName;
    public LoadTask(T taskName) {
      this.taskName = taskName;
    }


    @Override
    public T call() throws Exception {
        Thread.sleep(100);
        System.out.printf(taskName+"thread sleep 100 mill");
        return taskName;
    }
    public static LoadTask factory(final String name){
        return new LoadTask(name);
    }

}
