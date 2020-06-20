package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingConnectException;
import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.RemotingProcessor;

import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:02 上午 2020/5/20
 */
public interface RemotingClient {
    void start() throws RemotingException;
    void shutdown() throws RemotingException;
    RemotingCommand invokeSync(final String addr, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException;
    void registerProcessor(final int requestCode, final RemotingProcessor processor,
                           final ExecutorService executor);
}
