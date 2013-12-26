package com.zero.orzprofiler.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午8:21
 */
public final class Events {

    private final static ExecutorService EXECUTOR;
    private final static ScheduledExecutorService SCHEDULER;
    static {
        EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),new DebugableThreadFactory("Event-execute" , true));
        SCHEDULER = Executors.newSingleThreadScheduledExecutor(new DebugableThreadFactory("envent-scheduler"));
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               dispose();
            }
        });
        //jvm close this  run thread
        thread.setUncaughtExceptionHandler(LogUncaughtExceptionHandler.getInstance());
        Runtime.getRuntime().addShutdownHook(thread);
    }
    private static void dispose(){
        try {
            do {
                EXECUTOR.shutdownNow();
            }while (!EXECUTOR.awaitTermination(800L, TimeUnit.MILLISECONDS));
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        try {
            do {
                SCHEDULER.shutdownNow();
            }while (!SCHEDULER.awaitTermination(800L,TimeUnit.MILLISECONDS));
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }

}

