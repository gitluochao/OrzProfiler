package com.zero.orzprofiler.profiler.router.exception;

/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午2:17
 */
public class LoadBalanceException extends Exception {
    public LoadBalanceException() {
    }

    public LoadBalanceException(String message) {
        super(message);
    }

    public LoadBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadBalanceException(Throwable cause) {
        super(cause);
    }
}
