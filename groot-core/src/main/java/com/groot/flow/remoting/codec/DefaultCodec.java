package com.groot.flow.remoting.codec;



import com.groot.flow.remoting.RemotingCommand;
import com.groot.flow.remoting.RemotingCommandBody;
import com.groot.flow.serializable.FastjsonSerializable;

import java.nio.ByteBuffer;

/**
 * @author : chenhaitao934
 * @date : 11:25 上午 2020/5/21
 */
public class DefaultCodec implements Codec{
    @Override
    public RemotingCommand decode(ByteBuffer byteBuffer) throws Exception {
        FastjsonSerializable serializable = new FastjsonSerializable();
        int length = byteBuffer.limit();
        int serializableId = byteBuffer.getInt();
        //int temp = byteBuffer.getInt();
        int headerLength = byteBuffer.getInt();
        byte[] headerData = new byte[headerLength];
        byteBuffer.get(headerData);

        RemotingCommand cmd = serializable.deserialize(headerData, RemotingCommand.class);

        int remaining = length - 4 - 4 - headerLength;

        if (remaining > 0) {

            int bodyLength = byteBuffer.getInt();
            int bodyClassLength = remaining - 4 - bodyLength;

            if (bodyLength > 0) {

                byte[] bodyData = new byte[bodyLength];
                byteBuffer.get(bodyData);

                byte[] bodyClassData = new byte[bodyClassLength];
                byteBuffer.get(bodyClassData);

                cmd.setBody((RemotingCommandBody) serializable.deserialize(bodyData, Class.forName(new String(bodyClassData))));
            }
        }
        return cmd;
    }

    @Override
    public ByteBuffer encode(RemotingCommand remotingCommand) throws Exception {
        FastjsonSerializable serializable = new FastjsonSerializable();
        // header length size
        int length = 4;

        // serializable id (int)
        length += 4;

        //  header data length
        byte[] headerData = serializable.serialize(remotingCommand);
        length += headerData.length;

        byte[] bodyData = null;
        byte[] bodyClass = null;

        RemotingCommandBody body = remotingCommand.getBody();

        if (body != null) {
            // body data
            bodyData = serializable.serialize(body);
            length += bodyData.length;

            bodyClass = body.getClass().getName().getBytes();
            length += bodyClass.length;

            length += 4;
        }

        ByteBuffer result = ByteBuffer.allocate(4 + length);

        // length
        result.putInt(length);

        // serializable Id
        result.putInt(1);

        // header length
        result.putInt(headerData.length);

        // header data
        result.put(headerData);

        if (bodyData != null) {
            //  body length
            result.putInt(bodyData.length);
            //  body data
            result.put(bodyData);
            // body class
            result.put(bodyClass);
        }

        result.flip();

        return result;
    }
}
