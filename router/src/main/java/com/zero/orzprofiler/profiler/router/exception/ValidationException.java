package com.zero.orzprofiler.profiler.router.exception;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 上午10:47
 */
public class ValidationException extends Exception {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
