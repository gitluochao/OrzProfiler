package com.zero.orzprofiler.util;

import java.util.concurrent.*;

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
        thread.setUncaughtExceptionHandler(LogUncaughtexceptionhandler.getInstance());
        Runtime.getRuntime().addShutdownHook(thread);


    }
    public static void enqueue(final Runnable runnable){
      EXECUTOR.execute(runnable);
    }
    public static ScheduledFuture<?> scheduledAtFixRate(Runnable runnable,final long period,final TimeUnit timeUnit){
        return SCHEDULER.scheduleAtFixedRate(envent(runnable),period,period,timeUnit);
    }
    private static Runnable envent(final Runnable runnable){
        return new Runnable() {
            @Override
            public void run() {
                EXECUTOR.execute(runnable);
            }
        };
    }
    public static void dispose(){
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

