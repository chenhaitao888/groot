package com.groot.flow.exception;

/**
 * @author : chenhaitao934
 * @date : 10:06 上午 2020/5/20
 */
public class RemotingException extends Exception{

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }
}
