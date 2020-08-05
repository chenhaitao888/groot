package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingConnectException;
import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.GrootProcessor;
import com.groot.flow.remoting.command.GrootCommand;

import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:02 上午 2020/5/20
 */
public interface GrootClient {
    void start() throws RemotingException;
    void shutdown() throws RemotingException;
    GrootCommand invokeSync(final String addr, final GrootCommand request,
                            final long timeoutMillis) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException;
    void registerProcessor(final int requestCode, final GrootProcessor processor,
                           final ExecutorService executor);

    void registerDefaultProcessor(final GrootProcessor processor, final ExecutorService executor);
}
