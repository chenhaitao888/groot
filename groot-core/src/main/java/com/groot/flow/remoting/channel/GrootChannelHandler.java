package com.groot.flow.remoting.channel;


import com.groot.flow.remoting.GrootChannelHandlerListener;

/**
 * @author : chenhaitao934
 * @date : 11:33 上午 2020/5/20
 */
public interface GrootChannelHandler {
    GrootChannelHandler addListener(GrootChannelHandlerListener listener);
}
