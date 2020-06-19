package com.groot.flow.remoting;

/**
 * @author : chenhaitao934
 * @date : 3:59 下午 2020/5/20
 */
public class GrootCommandHelper {
    private static final int RPC_TYPE = 0; // 0, REQUEST_COMMAND
    private static final int RPC_ONEWAY = 1; // 1, RPC

    public static void markResponseType(GrootCommand remotingCommand) {
        int bits = 1 << RPC_TYPE;
        remotingCommand.setFlag(remotingCommand.getFlag() | bits);
    }

    public static boolean isResponseType(GrootCommand remotingCommand) {
        int bits = 1 << RPC_TYPE;
        return (remotingCommand.getFlag() & bits) == bits;
    }

    public static void markOnewayRPC(GrootCommand remotingCommand) {
        int bits = 1 << RPC_ONEWAY;
        remotingCommand.setFlag(remotingCommand.getFlag() | bits);
    }

    public static boolean isOnewayRPC(GrootCommand remotingCommand) {
        int bits = 1 << RPC_ONEWAY;
        return (remotingCommand.getFlag() & bits) == bits;
    }

    public static GrootCommandType getRemotingCommandType(GrootCommand remotingCommand) {
        if (GrootCommandHelper.isResponseType(remotingCommand)) {
            return GrootCommandType.RESPONSE_COMMAND;
        }
        return GrootCommandType.REQUEST_COMMAND;
    }
}
