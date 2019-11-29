package com.groot.flow.registry;

import com.groot.flow.constant.EXGrootConfig;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.spi.SPI;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
@SPI(key = EXGrootConfig.GROOT_ZK, value = "curator")
public interface ZkAdapter {
    ZkAPI getZkAPI(GrootConfig config);
}
