package com.groot.flow.remoting;


import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.logger.Logger;
import com.groot.flow.processor.GrootProcessor;
import com.groot.flow.processor.ServerProcessor;
import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.remoting.codec.GrootCodec;
import com.groot.flow.remoting.codec.DefaultCodec;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.remoting.command.GrootCommandHelper;
import com.groot.flow.utils.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author : chenhaitao934
 * @date : 10:16 上午 2020/5/20
 */
public abstract class AbstractRemoting {
    public Logger logger = LoggerFactory.getLogger(AbstractRemoting.class.getName());
    protected ServerProcessor processor = new ServerProcessor();
    protected final ConcurrentHashMap<Integer, GrootResponseFuture> responseTable =
            new ConcurrentHashMap<Integer, GrootResponseFuture>(256);
    protected final HashMap<Integer, Pair<GrootProcessor, ExecutorService>> processorTables = new HashMap<>(64);
    protected Pair<GrootProcessor, ExecutorService> defaultRequestProcessor;

    //protected final ConcurrentHashMap<Integer, RemotingProcessor> processorTables = new ConcurrentHashMap<>();
    public void processMessageReceived(GrootChannel channel, GrootCommand command) {
        if (command != null) {
            switch (GrootCommandHelper.getRemotingCommandType(command)) {
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

    private void processResponseCommand(GrootChannel channel, GrootCommand command) {
        logger.info("thread {} begin process {}'s reponse", Thread.currentThread(), GrootRemotingHelper.parseChannelRemoteAddr(channel));
        final GrootResponseFuture responseFuture = responseTable.get(command.getOpaque());
        if (responseFuture == null) {
            logger.info("receive response, but not matched any request");
            return;
        }
        responseFuture.putResponse(command);
    }

    private void processRequestCommand(GrootChannel channel, GrootCommand command) {
        logger.info("{} begin handle request from {}", channel.localAddress(), channel.remoteAddress());
        final Pair<GrootProcessor, ExecutorService> mathedPair = processorTables.get(command.getCode());
        final Pair<GrootProcessor, ExecutorService> processorExecutorPair = null == mathedPair ? this.defaultRequestProcessor : mathedPair;
        processorExecutorPair.getValue().submit(() -> {
            try {
                GrootCommand response = processorExecutorPair.getKey().processRequest(channel, command);
                response.setOpaque(command.getOpaque());
                channel.writeAndFlush(response).addListener(future -> {
                    if (!future.isSuccess()) {
                        logger.info("response to {} failed: {}", GrootRemotingHelper.parseChannelRemoteAddr(channel), future.cause());
                    } else {
                        logger.info("response to {} success", GrootRemotingHelper.parseChannelRemoteAddr(channel));
                    }
                });
            } catch (Exception e) {
                logger.error("process request failed", e);
            }
        });
    }

    public GrootCommand invokeSyncImpl(final GrootChannel channel, final GrootCommand request,
                                       final long timeoutMillis) throws InterruptedException, RemotingTimeoutException, RemotingSendRequestException {
        try {
            final GrootResponseFuture responseFuture =
                    new GrootResponseFuture(request.getOpaque(), timeoutMillis, null, null);
            this.responseTable.put(request.getOpaque(), responseFuture);
            channel.writeAndFlush(request).addListener(future -> {
                if (future.isSuccess()) {
                    responseFuture.setSendRequestOK(true);
                    return;
                } else {
                    responseFuture.setSendRequestOK(false);
                }
                responseTable.remove(request.getOpaque());
                responseFuture.setCause(future.cause());
                responseFuture.putResponse(null);
            });

            GrootCommand responseCommand = responseFuture.waitResponse(timeoutMillis);
            if (null == responseCommand) {
                /**发送请求成功，读取应答超时*/
                if (responseFuture.isSendRequestOK()) {
                    logger.info("read {} response failed", GrootRemotingHelper.parseChannelRemoteAddr(channel));
                    throw new RemotingTimeoutException(GrootRemotingHelper.parseChannelRemoteAddr(channel),
                            timeoutMillis, responseFuture.getCause());
                }
                /**发送请求失败*/
                else {
                    throw new RemotingSendRequestException(GrootRemotingHelper.parseChannelRemoteAddr(channel),
                            responseFuture.getCause());
                }
            }
            logger.info("read {} reponse success", GrootRemotingHelper.parseChannelRemoteAddr(channel));
            return responseCommand;
        } finally {
            this.responseTable.remove(request.getOpaque());
        }
    }

    protected GrootCodec getCodec() {
        return new DefaultCodec();
    }

}
