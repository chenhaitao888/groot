package com.groot.flow.netty;


import com.groot.flow.remoting.GrootChannelFuture;
import com.groot.flow.remoting.channel.GrootChannel;
import io.netty.channel.ChannelFuture;

/**
 * @author : chenhaitao934
 * @date : 8:09 下午 2020/5/21
 */
public class NettyChannelFuture implements GrootChannelFuture {

    private ChannelFuture future;

    public NettyChannelFuture(ChannelFuture future) {
        this.future = future;
    }

    @Override
    public boolean isConnected() {
        return future != null & future.channel().isActive();
    }

    @Override
    public GrootChannel getChannel() {
        return new NettyChannel(future.channel());
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return future.awaitUninterruptibly(timeoutMillis);
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public Throwable cause() {
        return future.cause();
    }
}
