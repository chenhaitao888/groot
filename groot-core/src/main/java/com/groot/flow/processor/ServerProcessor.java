package com.groot.flow.processor;


import com.groot.flow.remoting.RemotingCommand;
import com.groot.flow.remoting.channel.RemotingChannel;

/**
 * @author : chenhaitao934
 * @date : 3:43 下午 2020/5/21
 */
public class ServerProcessor implements RemotingProcessor{

    @Override
    public RemotingCommand processRequest(RemotingChannel channel, RemotingCommand request) throws Exception {
        System.out.println(request.getRemark());
        return RemotingCommand.createResponseCommand(0, "处理成功了", null);
    }
}
