package com.groot.flow.processor;


import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.remoting.channel.GrootChannel;

/**
 * @author : chenhaitao934
 * @date : 3:43 下午 2020/5/21
 */
public class ServerProcessor implements GrootProcessor{

    @Override
    public GrootCommand processRequest(GrootChannel channel, GrootCommand request) throws Exception {
        System.out.println(request.getRemark());
        return GrootCommand.createResponseCommand(0, "处理成功了", null);
    }
}
