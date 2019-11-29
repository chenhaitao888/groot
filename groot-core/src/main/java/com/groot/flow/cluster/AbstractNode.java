package com.groot.flow.cluster;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.exception.UnknownGenericTypeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenhaitao
 * @date 2019-11-29
 */
public abstract class AbstractNode<T extends AbstractNode> {
    private T t;
    private ObjectMapper yamlMapper = createMapper(new YAMLFactory(), null);
    public T fromYAML(InputStream in) throws IOException {
        T data = fromYAML(in, getGenericType());
        return data;
    }
    public <T> T fromYAML(InputStream inputStream, Class<T> configType) throws IOException {
        String content = resolveEnvParams(new InputStreamReader(inputStream));
        return yamlMapper.readValue(content, configType);
    }

    private String resolveEnvParams(Readable in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        try {
            if (s.hasNext()) {
                return resolveEnvParams(s.next());
            }
            return "";
        } finally {
            s.close();
        }
    }
    private String resolveEnvParams(String content) {
        Pattern pattern = Pattern.compile("\\$\\{(\\w+(:-.+)?)\\}");
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            String[] parts = m.group(1).split(":-");
            String v = System.getenv(parts[0]);
            if (v != null) {
                content = content.replace(m.group(), v);
            } else if (parts.length == 2) {
                content = content.replace(m.group(), parts[1]);
            }
        }
        return content;
    }

    private ObjectMapper createMapper(JsonFactory mapping, ClassLoader classLoader) {
        ObjectMapper mapper = new ObjectMapper(mapping);
        mapper.addMixIn(GrootConfig.class, GrootConfigMixIn.class);
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept());
        mapper.setFilterProvider(filterProvider);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (classLoader != null) {
            TypeFactory tf = TypeFactory.defaultInstance()
                    .withClassLoader(classLoader);
            mapper.setTypeFactory(tf);
        }
        return mapper;
    }
    public static class GrootConfigMixIn{
        @JsonProperty
        String registryAddress;
        @JsonProperty
        String clusterName;
        @JsonProperty
        Map<String, String> parameters;


    }

    private Class<T> getGenericType() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new UnknownGenericTypeException();
    }
}
