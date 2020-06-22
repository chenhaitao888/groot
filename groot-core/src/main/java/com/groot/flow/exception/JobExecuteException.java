package com.groot.flow.exception;

/**
 * @author : chenhaitao934
 * @date : 4:28 下午 2020/6/22
 */
public class JobExecuteException extends RuntimeException{
    public JobExecuteException() {
        super();
    }

    public JobExecuteException(String message) {
        super(message);
    }

    public JobExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobExecuteException(Throwable cause) {
        super(cause);
    }

    protected JobExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
