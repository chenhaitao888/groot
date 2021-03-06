package com.groot.flow.netty.client;


import com.groot.flow.exception.*;
import com.groot.flow.factory.NamedThreadFactory;
import com.groot.flow.netty.GrootCodecFactory;
import com.groot.flow.netty.NettyChannel;
import com.groot.flow.netty.NettyChannelFuture;
import com.groot.flow.remoting.AbstractRemotingClient;
import com.groot.flow.remoting.AsyncCallback;
import com.groot.flow.remoting.GrootChannelFuture;
import com.groot.flow.remoting.GrootClientConfig;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.remoting.channel.GrootChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.SocketAddress;

import static com.groot.flow.remoting.GrootRemotingHelper.closeChannel;
import static com.groot.flow.remoting.GrootRemotingHelper.parseChannelRemoteAddr;


/**
 * @author : chenhaitao934
 * @date : 2:08 下午 2020/5/21
 */
public class GrootRemotingClient extends AbstractRemotingClient {
    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroup;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;

    public GrootRemotingClient(GrootClientConfig remotingClientConfig) {
        super(remotingClientConfig);
        this.eventLoopGroup = new NioEventLoopGroup(remotingClientConfig.getClientSelectorThreads(), new NamedThreadFactory("NettyClientSelectorThread_", true));
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                remotingClientConfig.getClientWorkerThreads(),
                new NamedThreadFactory("NettyClientWorkerThread_"));
    }

    @Override
    protected void clientStart() throws RemotingException {

        final GrootCodecFactory nettyCodecFactory = new GrootCodecFactory(getCodec());

        this.bootstrap.group(this.eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        defaultEventExecutorGroup,
                        nettyCodecFactory.getEncoder(),
                        nettyCodecFactory.getDecoder(),
                        new IdleStateHandler(remotingClientConfig.getReaderIdleTimeSeconds(), remotingClientConfig.getWriterIdleTimeSeconds(), remotingClientConfig.getClientChannelMaxIdleTimeSeconds()),//
                        new NettyConnectManageHandler(),
                        new NettyClientHandler());
            }
        });

    }

    @Override
    protected void clientShutdown() {
        this.eventLoopGroup.shutdownGracefully();

        if (this.defaultEventExecutorGroup != null) {
            this.defaultEventExecutorGroup.shutdownGracefully();
        }
    }

    @Override
    protected GrootChannelFuture connect(SocketAddress socketAddress) {
        ChannelFuture channelFuture = this.bootstrap.connect(socketAddress);
        return new NettyChannelFuture(channelFuture);
    }


    class NettyClientHandler extends SimpleChannelInboundHandler<GrootCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, GrootCommand msg) throws Exception {
            processMessageReceived(new NettyChannel(ctx), msg);
        }
    }

    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
                            SocketAddress localAddress, ChannelPromise promise) throws Exception {
            final String local = localAddress == null ? "UNKNOW" : localAddress.toString();
            final String remote = remoteAddress == null ? "UNKNOW" : remoteAddress.toString();
            super.connect(ctx, remoteAddress, localAddress, promise);

            /*if (channelEventListener != null) {
                assert remoteAddress != null;
                putRemotingEvent(new RemotingEvent(RemotingEventType.CONNECT, remoteAddress
                        .toString(), new NettyChannel(ctx)));
            }*/
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

            GrootChannel channel = new NettyChannel(ctx);

            final String remoteAddress = parseChannelRemoteAddr(channel);
            closeChannel(channel);
            super.disconnect(ctx, promise);

            /*if (channelEventListener != null) {
                putRemotingEvent(new RemotingEvent(RemotingEventType.CLOSE, remoteAddress, channel));
            }*/
        }


        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            GrootChannel channel = new NettyChannel(ctx);

            final String remoteAddress = parseChannelRemoteAddr(channel);
            closeChannel(channel);
            super.close(ctx, promise);

            /*if (channelEventListener != null) {
                putRemotingEvent(new RemotingEvent(RemotingEventType.CLOSE, remoteAddress, channel));
            }*/
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            GrootChannel channel = new NettyChannel(ctx);

            final String remoteAddress = parseChannelRemoteAddr(channel);
            closeChannel(channel);
           /* if (channelEventListener != null) {
                putRemotingEvent(new RemotingEvent(RemotingEventType.EXCEPTION, remoteAddress, channel));
            }*/
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;

                GrootChannel channel = new NettyChannel(ctx);

                final String remoteAddress = parseChannelRemoteAddr(channel);

                if (event.state().equals(io.netty.handler.timeout.IdleState.ALL_IDLE)) {
                    closeChannel(channel);
                }

                /*if (channelEventListener != null) {
                    RemotingEventType remotingEventType = RemotingEventType.valueOf(event.state().name());
                    putRemotingEvent(new RemotingEvent(remotingEventType,
                            remoteAddress, channel));
                }*/
            }
            ctx.fireUserEventTriggered(evt);
        }
    }
}
