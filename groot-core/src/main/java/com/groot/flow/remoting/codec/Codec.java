package com.groot.flow.remoting.codec;


import com.groot.flow.remoting.RemotingCommand;

import java.nio.ByteBuffer;

/**
 * @author : chenhaitao934
 * @date : 11:23 上午 2020/5/21
 */
public interface Codec {
    RemotingCommand decode(final ByteBuffer buffer) throws Exception;
    ByteBuffer encode(final RemotingCommand remotingCommand) throws Exception;
}
