package com.groot.flow.exception;

/**
 * @author : chenhaitao934
 * @date : 10:10 上午 2020/8/6
 * 异步调用，请求数量超过信号量抛出此异常
 */
public class RemotingTooMuchRequestException extends RemotingException{
    public RemotingTooMuchRequestException(String message) {
        super(message);
    }
}
