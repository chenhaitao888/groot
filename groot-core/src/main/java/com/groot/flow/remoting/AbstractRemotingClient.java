package com.groot.flow.remoting;

import com.groot.flow.exception.RemotingConnectException;
import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.RemotingProcessor;
import com.groot.flow.remoting.channel.RemotingChannel;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 2:08 下午 2020/5/21
 */
public abstract class AbstractRemotingClient extends AbstractRemoting implements RemotingClient {
    protected final RemotingClientConfig remotingClientConfig;
    protected final ConcurrentHashMap<String, ChannelWrapper> channelTables = new ConcurrentHashMap<>();

    protected AbstractRemotingClient(RemotingClientConfig remotingClientConfig) {
        this.remotingClientConfig = remotingClientConfig;
    }
    @Override
    public void start() throws RemotingException {
        clientStart();
    }

    @Override
    public void shutdown() throws RemotingException {
        clientShutdown();
    }

    @Override
    public RemotingCommand invokeSync(String addr, RemotingCommand request, long timeoutMillis) throws InterruptedException, RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException {
        RemotingChannel channel = null;
        try {
            channel = getAndCreateChannel(addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(channel == null){
            return null;
        }
        return this.invokeSyncImpl(channel, request, timeoutMillis);
    }

    @Override
    public void registerProcessor(int requestCode, RemotingProcessor processor, ExecutorService executor) {
        processorTables.put(requestCode, processor);
    }

    private RemotingChannel getAndCreateChannel(final String addr) throws Exception {

        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isConnected()) {
            return cw.getChannel();
        }

        synchronized (this){
            boolean createNewConnection = false;
            cw = this.channelTables.get(addr);
            if (cw != null) {
                if(cw.isConnected()){
                    return cw.getChannel();
                }else if(!cw.getChannelFuture().isDone()) { //说明正在连接
                    createNewConnection = false;
                }else { //说明连接已经断开
                    this.channelTables.remove(addr);
                    createNewConnection = true;
                }
            }else {
                createNewConnection = true;
            }
            if(createNewConnection){
                RemotingChannelFuture channelFuture = connect(RemotingHelper.string2SocketAddress(addr));
                cw = new ChannelWrapper(channelFuture);
            }
        }
        if(cw != null){
            RemotingChannelFuture channelFuture = cw.getChannelFuture();
            if (channelFuture.awaitUninterruptibly(this.remotingClientConfig.getConnectTimeoutMillis())) {
                if(channelFuture.isConnected()){
                    return cw.getChannel();
                }
            }
        }
        return null;
    }



    protected abstract void clientStart() throws RemotingException;
    protected abstract void clientShutdown() throws RemotingException;
    protected abstract RemotingChannelFuture connect(SocketAddress socketAddress);
    
    class ChannelWrapper {
        private RemotingChannelFuture channelFuture;

        public ChannelWrapper(RemotingChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        public RemotingChannelFuture getChannelFuture() {
            return channelFuture;
        }

        public void setChannelFuture(RemotingChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        public boolean isConnected() {
            return channelFuture.isConnected();
        }
        private RemotingChannel getChannel() {
            return channelFuture.getChannel();
        }
    }
}
