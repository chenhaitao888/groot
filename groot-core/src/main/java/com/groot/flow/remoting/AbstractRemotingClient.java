package com.groot.flow.remoting;

import com.groot.flow.exception.*;
import com.groot.flow.processor.GrootProcessor;
import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.utils.Pair;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 2:08 下午 2020/5/21
 */
public abstract class AbstractRemotingClient extends AbstractRemoting implements GrootClient {
    protected final GrootClientConfig remotingClientConfig;
    protected final ConcurrentHashMap<String, ChannelWrapper> channelTables = new ConcurrentHashMap<>();

    protected AbstractRemotingClient(GrootClientConfig remotingClientConfig) {
        super(remotingClientConfig.getClientAsyncSemaphoreValue());
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
    public GrootCommand invokeSync(String addr, GrootCommand request, long timeoutMillis) throws InterruptedException, RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException {
        GrootChannel channel = getAndCreateChannel(addr);
        if (channel != null && channel.isConnected()) {
            try {
                return this.invokeSyncImpl(channel, request, timeoutMillis);
            } catch (RemotingSendRequestException e) {
                logger.error("invokeSync: send request exception, so close the channel{}", addr);
                GrootRemotingHelper.closeChannel(channel);
                throw e;
            }
        }else {
            GrootRemotingHelper.closeChannel(channel);
            throw new RemotingConnectException(addr);
        }
    }

    @Override
    public void invokeAsync(String addr, GrootCommand request, long timeoutMillis, AsyncCallback callback) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, RemotingTooMuchRequestException {
        GrootChannel channel = getAndCreateChannel(addr);
        if (channel != null && channel.isConnected()) {
            try {
                this.invokeAsyncImpl(channel, request, timeoutMillis, callback);
            } catch (RemotingSendRequestException e) {
                logger.error("invokeSync: send request exception, so close the channel{}", addr);
                GrootRemotingHelper.closeChannel(channel);
                throw e;
            }
        }else {
            GrootRemotingHelper.closeChannel(channel);
            throw new RemotingConnectException(addr);
        }
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

    private GrootChannel getAndCreateChannel(final String addr){

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
                }else if(!cw.getChannelFuture().isDone()) {
                    /**正在连接*/
                    createNewConnection = false;
                }else {
                    /**连接已经断开*/
                    this.channelTables.remove(addr);
                    createNewConnection = true;
                }
            }else {
                createNewConnection = true;
            }
            if(createNewConnection){
                GrootChannelFuture channelFuture = connect(GrootRemotingHelper.string2SocketAddress(addr));
                cw = new ChannelWrapper(channelFuture);
            }
        }
        if(cw != null){
            GrootChannelFuture channelFuture = cw.getChannelFuture();
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
    protected abstract GrootChannelFuture connect(SocketAddress socketAddress);
    
    class ChannelWrapper {
        private GrootChannelFuture channelFuture;

        public ChannelWrapper(GrootChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        public GrootChannelFuture getChannelFuture() {
            return channelFuture;
        }

        public void setChannelFuture(GrootChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        public boolean isConnected() {
            return channelFuture.isConnected();
        }
        private GrootChannel getChannel() {
            return channelFuture.getChannel();
        }
    }
}
