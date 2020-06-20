package com.groot.flow.remoting;

/**
 * @author : chenhaitao934
 * @date : 3:28 下午 2020/5/20
 */
public interface RemotingChannelHandlerListener {
    void operationComplete(Future future) throws Exception;
}
