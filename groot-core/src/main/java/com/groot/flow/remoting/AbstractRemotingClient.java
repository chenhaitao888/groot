package com.groot.flow.remoting;

import com.groot.flow.exception.RemotingConnectException;
import com.groot.flow.exception.RemotingException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.GrootProcessor;
import com.groot.flow.processor.GrootProcessor;
import com.groot.flow.remoting.channel.GrootChannel;
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
        GrootChannel channel = null;
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
    public void registerProcessor(int requestCode, GrootProcessor processor, ExecutorService executor) {
        Pair<GrootProcessor, ExecutorService> pair = new Pair<>(processor, executor);
        processorTables.put(requestCode, pair);
    }

    @Override
    public void registerDefaultProcessor(GrootProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessor = new Pair<>(processor, executor);
    }

    private GrootChannel getAndCreateChannel(final String addr) throws Exception {

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
