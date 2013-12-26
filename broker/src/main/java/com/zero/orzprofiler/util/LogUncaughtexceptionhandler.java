package com.zero.orzprofiler.util;

import org.apache.log4j.Logger;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午8:53
 */
public class LogUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private final static Logger log = Logger.getLogger(LogUncaughtExceptionHandler.class);

    public LogUncaughtExceptionHandler() {
    }
    private final static UncaughtExceptionHandler INSTANCE = new LogUncaughtExceptionHandler();

    public static UncaughtExceptionHandler getInstance(){
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(e instanceof RuntimeException && e.getCause() != null){
            if (e.getCause() instanceof InterruptedException)
                Thread.currentThread().interrupt();
            log.error(t.getName(),e.getCause());
        }
        log.error(t.getName(),e);
    }
}
