package com.groot.flow.registry;

import com.groot.flow.GrootContext;
import com.groot.flow.cluster.GrootNode;
import com.groot.flow.factory.ZkFactory;

/**
 * @author chenhaitao
 * @date 2019-11-27
 * zookeeper注册中心
 */
public class ZkRegistry extends AbstractRegistry{
    private ZkAPI zkApi;

    public ZkRegistry(GrootContext context) {
        this.zkApi = ZkFactory.getZkAPI(context.getConfig());
    }

    @Override
    protected void doRegister(GrootNode node) {
        if (zkApi.exists(node.toFullString())) {
            return;
        }
        zkApi.createNode(node.toFullString(), null,true, true);
        zkApi.getSelector().start();
    }

    @Override
    protected void doUnRegister(GrootNode node) {

    }


}
