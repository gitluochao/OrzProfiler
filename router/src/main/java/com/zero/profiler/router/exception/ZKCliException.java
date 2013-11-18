package com.zero.profiler.router.exception;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 下午6:02
 */
public class ZKCliException extends  Exception {
    public ZKCliException() {
    }

    public ZKCliException(String message) {
        super(message);
    }

    public ZKCliException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZKCliException(Throwable cause) {
        super(cause);
    }
}
