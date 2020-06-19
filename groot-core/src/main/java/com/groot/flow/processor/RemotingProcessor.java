package com.groot.flow.processor;

import com.groot.flow.remoting.RemotingCommand;
import com.groot.flow.remoting.channel.RemotingChannel;

public interface RemotingProcessor {
    RemotingCommand processRequest(RemotingChannel channel, RemotingCommand request) throws Exception;
}
