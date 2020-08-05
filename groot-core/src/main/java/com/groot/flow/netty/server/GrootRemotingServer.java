package com.groot.flow.netty.server;


import com.groot.flow.exception.RemotingException;
import com.groot.flow.factory.NamedThreadFactory;
import com.groot.flow.netty.NettyChannel;
import com.groot.flow.netty.GrootCodecFactory;
import com.groot.flow.remoting.AbstractRemotingServer;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.remoting.GrootRemotingHelper;
import com.groot.flow.remoting.GrootServerConfig;
import com.groot.flow.remoting.channel.GrootChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author : chenhaitao934
 * @date : 10:43 上午 2020/5/20
 */
public class GrootRemotingServer extends AbstractRemotingServer {
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup bossSelectorGroup;
    private final EventLoopGroup workerSelectorGroup;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    public GrootRemotingServer(GrootServerConfig remotingServerConfig) {
        super(remotingServerConfig);
        this.serverBootstrap = new ServerBootstrap();
        this.bossSelectorGroup = new NioEventLoopGroup(1, new NamedThreadFactory("NettyBossSelectorThread_"));
        this.workerSelectorGroup = new NioEventLoopGroup(remotingServerConfig.getServerSelectorThreads(), new NamedThreadFactory("NettyServerSelectorThread_", true));
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                remotingServerConfig.getServerWorkerThreads(),
                new NamedThreadFactory("NettyServerWorkerThread_")
        );
    }
    @Override
    protected void serverStart() throws RemotingException {
        final GrootCodecFactory nettyCodecFactory = new GrootCodecFactory(getCodec());
        this.serverBootstrap.group(this.bossSelectorGroup, this.workerSelectorGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 65536)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .localAddress(new InetSocketAddress(this.remotingServerConfig.getListenPort()))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                defaultEventExecutorGroup,
                                nettyCodecFactory.getEncoder(),
                                nettyCodecFactory.getDecoder(),
                                new IdleStateHandler(remotingServerConfig.getReaderIdleTimeSeconds(),
                                        remotingServerConfig.getWriterIdleTimeSeconds(), remotingServerConfig.getServerChannelMaxIdleTimeSeconds()),
                                new NettyConnectManageHandler(),
                                new NettyServerHandler());
                    }
                });

        try {
            this.serverBootstrap.bind().sync();
        } catch (InterruptedException e) {
            throw new RemotingException("Start Netty server bootstrap error", e);
        }
    }

    @Override
    protected void serverShutdown() throws RemotingException {
        this.bossSelectorGroup.shutdownGracefully();
        this.workerSelectorGroup.shutdownGracefully();

        if (this.defaultEventExecutorGroup != null) {
            this.defaultEventExecutorGroup.shutdownGracefully();
        }
    }

    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = GrootRemotingHelper.parseChannelRemoteAddr(new NettyChannel(ctx));
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            super.channelRegistered(ctx);
        }


        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = GrootRemotingHelper.parseChannelRemoteAddr(new NettyChannel(ctx));
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            GrootChannel channel = new NettyChannel(ctx);
            final String remoteAddress = GrootRemotingHelper.parseChannelRemoteAddr(channel);
            super.channelActive(ctx);

            /*if (channelEventListener != null) {
                putRemotingEvent(new RemotingEvent(RemotingEventType.CONNECT, remoteAddress, channel));
            }*/
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            GrootChannel channel = new NettyChannel(ctx);

            final String remoteAddress = GrootRemotingHelper.parseChannelRemoteAddr(channel);
            super.channelInactive(ctx);

            /*if (channelEventListener != null) {
                putRemotingEvent(new RemotingEvent(RemotingEventType.CLOSE, remoteAddress, channel));
            }*/
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;

                GrootChannel channel = new NettyChannel(ctx);

                final String remoteAddress = GrootRemotingHelper.parseChannelRemoteAddr(channel);

                if (event.state().equals(IdleState.ALL_IDLE)) {
                    GrootRemotingHelper.closeChannel(channel);
                }

                /*if (channelEventListener != null) {
                    RemotingEventType remotingEventType = RemotingEventType.valueOf(event.state().name());
                    putRemotingEvent(new RemotingEvent(remotingEventType,
                            remoteAddress, channel));
                }*/
            }

            ctx.fireUserEventTriggered(evt);
        }


        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            NettyChannel nettyChannel = new NettyChannel(channel);
            String addr = GrootRemotingHelper.parseChannelRemoteAddr(nettyChannel);
            logger.info("{} has beean connected", addr);
            String[] s = addr.split(":");
            channelMap.put(s[0], nettyChannel);

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            GrootChannel channel = new NettyChannel(ctx);

            final String remoteAddress = GrootRemotingHelper.parseChannelRemoteAddr(channel);

            /*if (channelEventListener != null) {
                putRemotingEvent(new RemotingEvent(RemotingEventType.EXCEPTION, remoteAddress, channel));
            }*/

            GrootRemotingHelper.closeChannel(channel);
        }
    }
    class NettyServerHandler extends SimpleChannelInboundHandler<GrootCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, GrootCommand msg) throws Exception {
            processMessageReceived(new NettyChannel(ctx), msg);
        }
    }
}
