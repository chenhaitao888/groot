package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.RemotingProcessor;
import com.groot.flow.remoting.channel.GrootChannel;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:01 上午 2020/5/20
 */
public interface GrootServer {
    void start() throws RemotingException;
    void shutdown() throws RemotingException;
    GrootCommand invokeSync(final GrootChannel channel, final GrootCommand request,
                            final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException;
    GrootChannel getChannel(List<String> address);
    void registerProcessor(final int requestCode, final RemotingProcessor processor,
                           final ExecutorService executor);

    void registerDefaultProcessor(final RemotingProcessor processor, final ExecutorService executor);
}
