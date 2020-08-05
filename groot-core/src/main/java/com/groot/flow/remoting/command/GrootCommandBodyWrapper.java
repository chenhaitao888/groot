package com.groot.flow.remoting.command;

import com.groot.flow.GrootContext;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.remoting.command.AbstractCommandBody;

/**
 * @author : chenhaitao934
 * @date : 9:07 下午 2020/8/5
 */
public class GrootCommandBodyWrapper {

    private GrootConfig grootConfig;

    public GrootCommandBodyWrapper(GrootConfig grootConfig) {
        this.grootConfig = grootConfig;
    }

    public <T extends AbstractCommandBody> T wrapper(T commandBody) {
        commandBody.setNodeType(grootConfig.getNodeType().name());
        commandBody.setIdentity(grootConfig.getIdentity());
        return commandBody;
    }

    public static <T extends AbstractCommandBody> T wrapper(GrootContext grootContext, T commandBody) {
        return grootContext.getWrapper().wrapper(commandBody);
    }
}
