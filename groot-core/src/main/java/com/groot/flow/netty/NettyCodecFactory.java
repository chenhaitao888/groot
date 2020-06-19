package com.groot.flow.netty;


import com.groot.flow.remoting.Future;
import com.groot.flow.remoting.RemotingChannelHandlerListener;
import com.groot.flow.remoting.RemotingCommand;
import com.groot.flow.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author : chenhaitao934
 * @date : 4:11 下午 2020/5/20
 */
public class NettyCodecFactory {
    private Codec codec;

    public NettyCodecFactory(Codec codec) {
        this.codec = codec;
    }

    public ChannelHandler getDecoder() {
        return new NettyDecoder();
    }

    class NettyDecoder extends LengthFieldBasedFrameDecoder {

        public NettyDecoder() {
            super(16 * 1024 * 1024, 0, 4, 0, 4);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            ByteBuf decode = (ByteBuf) super.decode(ctx, in);
            if (decode == null) {
                return null;
            }

            byte[] tmpBuf = new byte[decode.capacity()];
            decode.getBytes(0, tmpBuf);
            decode.release();

            ByteBuffer byteBuffer = ByteBuffer.wrap(tmpBuf);
            return codec.decode(byteBuffer);
        }
    }

    class NettyEncoder extends MessageToByteEncoder<RemotingCommand>{

        @Override
        protected void encode(ChannelHandlerContext ctx, RemotingCommand remotingCommand, ByteBuf out) throws Exception {
            ByteBuffer byteBuffer = codec.encode(remotingCommand);
            out.writeBytes(byteBuffer);
        }
    }
    public ChannelHandler getEncoder() {
        return new NettyEncoder();
    }


    public static void main(String[] args) throws Exception{
        /*DefaultCodec defaultCodec = new DefaultCodec();
        RemotingCommand command = new RemotingCommand();
        RemotingCommandBody body = new RemotingCommandBody();
        body.setIp("192.168.1.1");
        body.setMsg("发送小心");
        body.setSuccess(true);
        command.setBody(body);
        command.setFlag(1);
        ByteBuffer encode = defaultCodec.encode(command);
        RemotingCommand decode = defaultCodec.decode(encode);
        System.out.println(decode);*/

        ChannelFuture future = new ChannelFuture() {
            @Override
            public Channel channel() {
                return null;
            }

            @Override
            public ChannelFuture addListener(GenericFutureListener<? extends io.netty.util.concurrent.Future<? super Void>> listener) {
                return null;
            }

            @Override
            public ChannelFuture addListeners(GenericFutureListener<? extends io.netty.util.concurrent.Future<? super Void>>... listeners) {
                return null;
            }

            @Override
            public ChannelFuture removeListener(GenericFutureListener<? extends io.netty.util.concurrent.Future<? super Void>> listener) {
                return null;
            }

            @Override
            public ChannelFuture removeListeners(GenericFutureListener<? extends io.netty.util.concurrent.Future<? super Void>>... listeners) {
                return null;
            }

            @Override
            public ChannelFuture sync() throws InterruptedException {
                return null;
            }

            @Override
            public ChannelFuture syncUninterruptibly() {
                return null;
            }

            @Override
            public ChannelFuture await() throws InterruptedException {
                return null;
            }

            @Override
            public ChannelFuture awaitUninterruptibly() {
                return null;
            }

            @Override
            public boolean isVoid() {
                return false;
            }

            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public boolean isCancellable() {
                return false;
            }

            @Override
            public Throwable cause() {
                return null;
            }

            @Override
            public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public boolean await(long timeoutMillis) throws InterruptedException {
                return false;
            }

            @Override
            public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
                return false;
            }

            @Override
            public boolean awaitUninterruptibly(long timeoutMillis) {
                return false;
            }

            @Override
            public Void getNow() {
                return null;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Void get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
        System.out.println(NettyEncoder.class.getSimpleName());
        NettyChannelHandler handler = new NettyChannelHandler(future);
        handler.addListener(new RemotingChannelHandlerListener() {
            @Override
            public void operationComplete(Future future) throws Exception {

            }
        });
    }
}
