package com.groot.flow.remoting;


import com.groot.flow.remoting.channel.GrootChannel;

/**
 * @author : chenhaitao934
 * @date : 8:08 下午 2020/5/21
 */
public interface GrootChannelFuture {
    boolean isConnected();

    GrootChannel getChannel();

    boolean awaitUninterruptibly(long timeoutMillis);

    boolean isDone();

    Throwable cause();
}
