package com.groot.flow.event;

import com.groot.flow.constant.EXGrootConfig;
import com.groot.flow.spi.SPI;
import com.groot.flow.constant.GrootConfig;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 事件中心接口
 */
@SPI(key = EXGrootConfig.GROOT_EVENT, value = "default")
public interface EventCenter {
    void subsribe(Subscriber subscriber, String... topics);
    void unSubsribe(String topic, Subscriber subscriber);
    void publish(EventInfo eventInfo);
    void publishSync(EventInfo eventInfo);
}
