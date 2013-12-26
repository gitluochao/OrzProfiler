package com.zero.orzprofiler.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午9:01
 */
public class DebugableThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final String namePrefix;
    private final boolean daemon;
    private final AtomicInteger threadNum = new AtomicInteger(1);

    public DebugableThreadFactory(String name) {
        this(name,false);
    }

    public DebugableThreadFactory(String name,boolean daemon) {
        this.daemon = daemon;
        final SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = name + "_thread_";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group,r,namePrefix + threadNum.getAndDecrement(),0);
        if (daemon) t.setDaemon(daemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
        t.setUncaughtExceptionHandler(LogUncaughtExceptionHandler.getInstance());
        return t;
    }
}
