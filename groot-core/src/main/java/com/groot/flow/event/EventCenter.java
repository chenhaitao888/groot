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
    void subscribe(EventSubscriber subscriber, String... topics);
    void unSubscribe(String topic, EventSubscriber subscriber);
    void publishSync(EventInfo eventInfo);
    void publishAsync(EventInfo eventInfo);

}
