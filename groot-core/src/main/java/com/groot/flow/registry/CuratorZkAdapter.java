package com.groot.flow.registry;

import com.groot.flow.constant.GrootConfig;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public class CuratorZkAdapter implements ZkAdapter {
    @Override
    public ZkAPI getZkAPI(GrootConfig config) {
        return new CuratorAPI(config);
    }
}
