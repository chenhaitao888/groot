package com.groot.flow.remoting;

import com.groot.flow.GrootContext;
import com.groot.flow.cluster.GrootNode;
import com.groot.flow.exception.RemotingConnectException;
import com.groot.flow.exception.RemotingSendRequestException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.logger.Logger;
import com.groot.flow.processor.AbstractProcessor;
import com.groot.flow.remoting.command.GrootCommand;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author : chenhaitao934
 * @date : 10:12 下午 2020/8/5
 */
public class GrootRemotingClientDelegate {

    public Logger logger = LoggerFactory.getLogger(AbstractProcessor.class.getName());

    private GrootClient grootClient;
    private GrootContext context;
    private volatile boolean serverEnable = false;
    private List<GrootNode> dispenseNodes;

    public GrootRemotingClientDelegate(GrootClient grootClient, GrootContext context) {
        this.grootClient = grootClient;
        this.context = context;
        this.dispenseNodes = new CopyOnWriteArrayList();
    }

    public GrootCommand invokeSync(GrootCommand requestCommand){
        GrootNode dispenseNode = getDispenseNode();
        try {
            GrootCommand response = grootClient.invokeSync(dispenseNode.getAddress(), requestCommand,
                    context.getConfig().getInvokeTimeoutMillis());
            this.serverEnable = true;
            return response;
        } catch (Exception e) {
            dispenseNodes.remove(dispenseNode);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), e);
            }
            // 轮询节点
            return invokeSync(requestCommand);
        }
    }

    public void invokeAsync(GrootCommand requestCommand, AsyncCallback callback){
        GrootNode dispenseNode = getDispenseNode();
        try {
            grootClient.invokeAsync(dispenseNode.getAddress(), requestCommand,
                    context.getConfig().getInvokeTimeoutMillis(), callback);
            this.serverEnable = true;
        } catch (Exception e) {
            dispenseNodes.remove(dispenseNode);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), e);
            }
            // 轮询节点
            invokeAsync(requestCommand, callback);
        }
    }

    private GrootNode getDispenseNode() {
        // todo 通过负载均衡获取node
        return dispenseNodes.get(0);
    }
}
