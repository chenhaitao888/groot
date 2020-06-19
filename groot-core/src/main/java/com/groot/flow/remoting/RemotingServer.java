package com.groot.flow.remoting;



import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.RemotingProcessor;
import com.groot.flow.remoting.channel.RemotingChannel;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:01 上午 2020/5/20
 */
public interface RemotingServer {
    void start() throws RemotingException;
    void shutdown() throws RemotingException;
    RemotingCommand invokeSync(final RemotingChannel channel, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException;
    RemotingChannel getChannel(List<String> address);
    void registerProcessor(final int requestCode, final RemotingProcessor processor,
                           final ExecutorService executor);
}
