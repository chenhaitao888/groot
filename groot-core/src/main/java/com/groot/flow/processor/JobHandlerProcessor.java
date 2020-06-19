package com.groot.flow.processor;



import com.groot.flow.remoting.JobPushRequest;
import com.groot.flow.remoting.RemotingCommand;
import com.groot.flow.remoting.channel.RemotingChannel;

import java.lang.reflect.Method;

/**
 * @author : chenhaitao934
 * @date : 9:01 下午 2020/5/22
 */
public class JobHandlerProcessor implements RemotingProcessor{
    @Override
    public RemotingCommand processRequest(RemotingChannel channel, RemotingCommand request) throws Exception {
        JobPushRequest body = (JobPushRequest) request.getBody();
        String className = body.getClassName();
        String methodName = body.getMethodName();
        Class<?> clazz = Class.forName(className);
        Object o = clazz.newInstance();
        Method method = clazz.getMethod(methodName);
        method.invoke(o);
        return RemotingCommand.createResponseCommand(0, "处理成功了", null);
    }
}
