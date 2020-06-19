package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.processor.RemotingProcessor;
import com.groot.flow.processor.ServerProcessor;
import com.groot.flow.remoting.channel.RemotingChannel;
import com.groot.flow.remoting.codec.Codec;
import com.groot.flow.remoting.codec.DefaultCodec;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : chenhaitao934
 * @date : 10:16 上午 2020/5/20
 */
public abstract class AbstractRemoting {
    protected ServerProcessor processor = new ServerProcessor();
    protected final ConcurrentHashMap<Integer /* opaque */, ResponseFuture> responseTable =
            new ConcurrentHashMap<Integer, ResponseFuture>(256);
    protected final ConcurrentHashMap<Integer, RemotingProcessor> processorTables = new ConcurrentHashMap<>();
    public void processMessageReceived(RemotingChannel channel, RemotingCommand command){
        if (command != null) {
            switch (RemotingCommandHelper.getRemotingCommandType(command)) {
                case REQUEST_COMMAND:
                    processRequestCommand(channel, command);
                    break;
                case RESPONSE_COMMAND:
                    processResponseCommand(channel, command);
                    break;
                default:
                    break;
            }
        }
    }

    private void processResponseCommand(RemotingChannel channel, RemotingCommand command) {
        System.out.println("线程" + Thread.currentThread() +"开始处理" + RemotingHelper.parseChannelRemoteAddr(channel) + "响应");
        final ResponseFuture responseFuture = responseTable.get(command.getOpaque());
        if(responseFuture == null){
            System.out.println("receive response, but not matched any request");
            return;
        }
        responseFuture.putResponse(command);
    }

    private void processRequestCommand(RemotingChannel channel, RemotingCommand command) {
        System.out.println("开始处理请求.....");
        try {
            final RemotingProcessor remotingProcessor = processorTables.get(command.getCode());
            if(remotingProcessor == null){
                return;
            }
            RemotingCommand response = remotingProcessor.processRequest(channel, command);
            response.setOpaque(command.getOpaque());
            channel.writeAndFlush(response).addListener(new RemotingChannelHandlerListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if(!future.isSuccess()){
                        System.out.println("response to " + RemotingHelper.parseChannelRemoteAddr(channel) + " failed:" + future.cause());
                    }else {
                        System.out.println("response to " + RemotingHelper.parseChannelRemoteAddr(channel) + "success");
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RemotingCommand invokeSyncImpl(final RemotingChannel channel, final RemotingCommand request,
                                          final long timeoutMillis) throws InterruptedException, RemotingTimeoutException, RemotingSendRequestException {
        try {
            System.out.println("线程：" + Thread.currentThread());
            final ResponseFuture responseFuture =
                    new ResponseFuture(request.getOpaque(), timeoutMillis, null, null);
            this.responseTable.put(request.getOpaque(), responseFuture);
            channel.writeAndFlush(request).addListener(new RemotingChannelHandlerListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if (future.isSuccess()) {
                        responseFuture.setSendRequestOK(true);
                        return;
                    } else {
                        responseFuture.setSendRequestOK(false);
                    }

                    responseTable.remove(request.getOpaque());
                    responseFuture.setCause(future.cause());
                    responseFuture.putResponse(null);
                }
            });

            RemotingCommand responseCommand = responseFuture.waitResponse(timeoutMillis);
            if (null == responseCommand) {
                // 发送请求成功，读取应答超时
                if (responseFuture.isSendRequestOK()) {
                    System.out.println("读取" + RemotingHelper.parseChannelRemoteAddr(channel) + "应答失败");
                    throw new RemotingTimeoutException(RemotingHelper.parseChannelRemoteAddr(channel),
                            timeoutMillis, responseFuture.getCause());
                }
                // 发送请求失败
                else {
                    throw new RemotingSendRequestException(RemotingHelper.parseChannelRemoteAddr(channel),
                            responseFuture.getCause());
                }
            }
            System.out.println("读取" + RemotingHelper.parseChannelRemoteAddr(channel) + "应答成功");
            return responseCommand;
        } finally {
            //this.responseTable.remove(request.getOpaque());
        }
    }
    protected Codec getCodec(){
        return new DefaultCodec();
    }

}
