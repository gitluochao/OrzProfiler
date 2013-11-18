package com.zero.profiler.router.exception;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 下午6:02
 */
public class ServiceException extends Exception {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
