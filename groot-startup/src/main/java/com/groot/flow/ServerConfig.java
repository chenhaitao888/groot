package com.groot.flow;

import com.groot.flow.cluster.AbstractNode;
import com.groot.flow.constant.GrootConfig;

/**
 * @author chenhaitao
 * @date 2019-11-29
 */

public class ServerConfig extends AbstractNode<ServerConfig> {
    private GrootConfig grootConfig;

    public GrootConfig getGrootConfig() {
        return grootConfig;
    }

    public void setGrootConfig(GrootConfig grootConfig) {
        this.grootConfig = grootConfig;
    }
}
