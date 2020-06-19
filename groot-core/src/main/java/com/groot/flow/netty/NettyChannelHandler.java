package com.groot.flow.netty;


import com.groot.flow.remoting.GrootFuture;
import com.groot.flow.remoting.GrootChannelHandlerListener;
import com.groot.flow.remoting.channel.GrootChannelHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author : chenhaitao934
 * @date : 11:34 上午 2020/5/20
 */
public class NettyChannelHandler implements GrootChannelHandler {
    private ChannelFuture future;
    public NettyChannelHandler(ChannelFuture future) {
        this.future = future;
    }

    @Override
    public GrootChannelHandler addListener(GrootChannelHandlerListener listener) {
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                listener.operationComplete(new GrootFuture() {
                    @Override
                    public boolean isSuccess() {
                        return future.isSuccess();
                    }

                    @Override
                    public Throwable cause() {
                        return future.cause();
                    }
                });
            }
        });
        return this;
    }
}
