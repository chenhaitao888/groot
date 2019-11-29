package com.groot.flow.constant;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhaitao
 * @date 2019-11-28
 */
@Setter
@Getter
public class GrootConfig {
    private String registryAddress;
    private String clusterName;
    private int listenPort;
    private int ip;
    private Map<String, String> parameters = new HashMap<>();
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public String toString() {
        return "GrootConfig{" +
                "registryAddress='" + registryAddress + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", listenPort=" + listenPort +
                ", parameters=" + parameters +
                '}';
    }
}
