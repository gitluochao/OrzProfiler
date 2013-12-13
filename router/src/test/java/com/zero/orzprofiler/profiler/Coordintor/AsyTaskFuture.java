package com.zero.orzprofiler.profiler.Coordintor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * User: luochao
 * Date: 13-11-8
 * Time: 涓嫔崃6:55
 */
public class AsyTaskFuture<T> extends FutureTask<T> {
    //private static final Logger log = Logger.getLogger(AsyTaskFuture.class);
    public AsyTaskFuture(Callable callable) {
        super(callable);
    }
    public T  getResult(){
        try{
            return super.get();
        }catch (ExecutionException e){
          Throwable cause = e.getCause();
          if(cause instanceof RuntimeException){

          }
        }catch (InterruptedException e){
            Thread.interrupted();
        }
        return null;
    }

}
