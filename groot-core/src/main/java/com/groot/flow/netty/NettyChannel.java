package com.groot.flow.netty;


import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.remoting.channel.GrootChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * @author : chenhaitao934
 * @date : 11:29 上午 2020/5/20
 */
public class NettyChannel implements GrootChannel {
    private Channel channel;
    public NettyChannel(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
    }

    public NettyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public GrootChannelHandler writeAndFlush(Object msg) {
        ChannelFuture channelFuture = channel.writeAndFlush(msg);
        return new NettyChannelHandler(channelFuture);
    }

    @Override
    public GrootChannelHandler close() {
        return new NettyChannelHandler(channel.close());
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public boolean isClosed() {
        return !isOpen();
    }
}
