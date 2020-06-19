package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.RemotingProcessor;
import com.groot.flow.remoting.channel.RemotingChannel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:38 上午 2020/5/20
 */
public abstract class AbstractRemotingServer extends AbstractRemoting implements RemotingServer {
    protected final RemotingServerConfig remotingServerConfig;
    public final Map<String, RemotingChannel> channelMap = new ConcurrentHashMap<>();
    protected AbstractRemotingServer(RemotingServerConfig remotingServerConfig) {
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
    public RemotingCommand invokeSync(RemotingChannel channel, RemotingCommand request, long timeoutMillis) throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {
        return this.invokeSyncImpl(channel, request, timeoutMillis);
    }

    @Override
    public RemotingChannel getChannel(List<String> addressList) {
        for(String address : addressList){
            RemotingChannel channel = getChannel(address);
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
    public void registerProcessor(int requestCode, RemotingProcessor processor, ExecutorService executor) {
        processorTables.put(requestCode, processor);
    }

    public RemotingChannel getChannel(String address){
        return channelMap.get(address);
    }
}
