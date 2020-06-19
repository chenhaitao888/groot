package com.groot.flow.processor;

import com.groot.flow.remoting.GrootCommand;
import com.groot.flow.remoting.channel.GrootChannel;

public interface RemotingProcessor {
    GrootCommand processRequest(GrootChannel channel, GrootCommand request) throws Exception;
}
