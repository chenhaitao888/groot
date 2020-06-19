package com.groot.flow.netty;


import com.groot.flow.remoting.Future;
import com.groot.flow.remoting.RemotingChannelHandlerListener;
import com.groot.flow.remoting.channel.RemotingChannelHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author : chenhaitao934
 * @date : 11:34 上午 2020/5/20
 */
public class NettyChannelHandler implements RemotingChannelHandler {
    private ChannelFuture future;
    public NettyChannelHandler(ChannelFuture future) {
        this.future = future;
    }

    @Override
    public RemotingChannelHandler addListener(RemotingChannelHandlerListener listener) {
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                listener.operationComplete(new Future() {
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
