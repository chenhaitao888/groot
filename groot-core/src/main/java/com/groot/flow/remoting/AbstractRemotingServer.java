package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.GrootProcessor;
import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:38 上午 2020/5/20
 */
public abstract class AbstractRemotingServer extends AbstractRemoting implements GrootServer {
    protected final GrootServerConfig remotingServerConfig;
    public final Map<String, GrootChannel> channelMap = new ConcurrentHashMap<>();
    protected AbstractRemotingServer(GrootServerConfig remotingServerConfig) {
        this.remotingServerConfig = remotingServerConfig;
    }

    protected abstract void serverStart() throws RemotingException;
    protected abstract void serverShutdown() throws RemotingException;
    @Override
    public void start() throws RemotingException {
        serverStart();
    }

    @Override
    public void shutdown() throws RemotingException {
        serverShutdown();
    }

    @Override
    public GrootCommand invokeSync(GrootChannel channel, GrootCommand request, long timeoutMillis) throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {
        return this.invokeSyncImpl(channel, request, timeoutMillis);
    }

    @Override
    public GrootChannel getChannel(List<String> addressList) {
        for(String address : addressList){
            GrootChannel channel = getChannel(address);
            if(channel == null){
                return null;
            }
            if(channel.isConnected()){
                return channel;
            }
        }
        return null;
    }

    @Override
    public void registerProcessor(int requestCode, GrootProcessor processor, ExecutorService executor) {
        Pair<GrootProcessor, ExecutorService> pair = new Pair<>(processor, executor);
        processorTables.put(requestCode, pair);
    }

    @Override
    public void registerDefaultProcessor(GrootProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessor = new Pair<>(processor, executor);
    }

    public GrootChannel getChannel(String address){
        return channelMap.get(address);
    }
}
