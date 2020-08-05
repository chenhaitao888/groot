package com.groot.flow;

import com.groot.flow.constant.GrootConfig;
import com.groot.flow.event.EventCenter;
import com.groot.flow.remoting.command.GrootCommandBodyWrapper;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public class GrootContext {
    private GrootConfig config;
    private EventCenter eventCenter;
    private GrootCommandBodyWrapper wrapper;

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

    public GrootCommandBodyWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(GrootCommandBodyWrapper wrapper) {
        this.wrapper = wrapper;
    }
}
