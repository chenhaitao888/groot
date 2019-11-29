package com.groot.flow.serializable;

import com.groot.flow.constant.EXGrootConfig;
import com.groot.flow.spi.SPI;

/**
 * @author chenhaitao
 * @date 2019-11-28
 * 序列化接口
 */
@SPI(key = EXGrootConfig.GROOT_SERIALIZABLE, value = "fastjson")
public interface GrootSerializable {
    byte[] serialize(final Object data);
    <T> T deserialize(final byte[] data, Class<T> clazz);
}
