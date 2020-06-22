package com.groot.flow;

import com.groot.flow.constant.GrootConfig;
import com.groot.flow.event.EventCenter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public class GrootContext {
    private GrootConfig config;
    private EventCenter eventCenter;
    public GrootConfig getConfig() {
        return config;
    }

    public void setConfig(GrootConfig config) {
        this.config = config;
    }

    public EventCenter getEventCenter() {
        return eventCenter;
    }

    public void setEventCenter(EventCenter eventCenter) {
        this.eventCenter = eventCenter;
    }
}
