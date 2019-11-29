package com.groot.flow.logger;

import com.groot.flow.constant.EXGrootConfig;
import com.groot.flow.spi.SPI;
import com.groot.flow.constant.GrootConfig;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 日志适配器,默认slf4j
 */
@SPI(key = EXGrootConfig.GROOT_LOGGER, value = "slf4j")
public interface LoggerAdapter {
    Logger getLogger(Class<?> key);
    Logger getLogger(String key);
}
