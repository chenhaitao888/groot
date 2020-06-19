package com.groot.flow.remoting.channel;


import java.net.SocketAddress;

/**
 * @author : chenhaitao934
 * @date : 11:22 上午 2020/5/20
 */
public interface RemotingChannel {
    SocketAddress localAddress();

    SocketAddress remoteAddress();

    RemotingChannelHandler writeAndFlush(Object msg);

    RemotingChannelHandler close();

    boolean isConnected();

    boolean isOpen();

    boolean isClosed();
}
