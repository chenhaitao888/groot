package com.groot.flow.serializable;

import com.groot.flow.constant.EXGrootConfig;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.utils.StringUtils;

/**
 * @author chenhaitao
 * @date 2019-11-28
 */
public class SerializableAdapter{
    private static String defaultSerializble;
    public static byte[] serialize(Object data) {
        GrootSerializable grootSerializable = getGrootSerializable(defaultSerializble);
        return grootSerializable.serialize(data);
    }

    public static  <T> T deserialize(byte[] data, Class<T> clazz) {
        GrootSerializable grootSerializable = getGrootSerializable(defaultSerializble);
        return grootSerializable.deserialize(data, clazz);
    }

    public static GrootSerializable getGrootSerializable(String serializable){
        GrootSerializable grootSerializable = null;//TODO
        return grootSerializable;
    }

    public void setDefaultSerializble(GrootConfig config){
        String parameter = config.getParameter(EXGrootConfig.GROOT_SERIALIZABLE);
        if(StringUtils.isNotEmpty(parameter)){
            this.defaultSerializble = parameter;
        }
    }
}
