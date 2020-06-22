package com.groot.flow.processor;

import com.groot.flow.remoting.GrootCommand;
import com.groot.flow.remoting.channel.GrootChannel;

public interface GrootProcessor {
    GrootCommand processRequest(GrootChannel channel, GrootCommand request) throws Exception;
}
