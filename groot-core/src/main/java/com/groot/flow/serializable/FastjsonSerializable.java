package com.groot.flow.serializable;

import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;

/**
 * @author chenhaitao
 * @date 2019-11-28
 */
public class FastjsonSerializable implements GrootSerializable{
    @Override
    public byte[] serialize(Object data) {
        String json = toJson(data, false);
        return json.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        final String json = new String(data, Charset.forName("UTF-8"));
        return fromJson(json, clazz);
    }
    private String toJson(final Object obj, boolean prettyFormat) {
        return JSON.toJSONString(obj, prettyFormat);
    }
    private <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }
}
