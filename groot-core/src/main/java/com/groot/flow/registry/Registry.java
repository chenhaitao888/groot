package com.groot.flow.registry;

import com.groot.flow.cluster.GrootNode;

/**
 * @author chenhaitao
 * @date 2019-11-27
 * 注册节点接口
 */
public interface Registry {
    void register(GrootNode node);
    void unregister(GrootNode node);
}
