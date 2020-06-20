package com.groot.flow.remoting;


import com.groot.flow.remoting.channel.RemotingChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author : chenhaitao934
 * @date : 11:21 上午 2020/5/20
 */
public class RemotingHelper {
    /**
     * IP:PORT
     */
    public static SocketAddress string2SocketAddress(final String addr) {
        String[] s = addr.split(":");
        return new InetSocketAddress(s[0], Integer.valueOf(s[1]));
    }

    public static String parseChannelRemoteAddr(final RemotingChannel channel) {
        if (null == channel) {
            return "";
        }
        final SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }

            return addr;
        }

        return "";
    }

    public static void closeChannel(RemotingChannel channel) {
        final String addrRemote = RemotingHelper.parseChannelRemoteAddr(channel);
        channel.close().addListener(new RemotingChannelHandlerListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                System.out.println(("closeChannel: close the connection to remote address[{}] result: {}" + addrRemote +
                        future.isSuccess()));
            }
        });
    }

}
