package com.groot.flow.constant;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhaitao
 * @date 2019-11-28
 */
public class GrootConfig {
    private String registryAddress;
    private String clusterName;
    private int listenPort;
    private int ip;
    private String id;
    private int workThreads;
    private int queueCapacity;
    private Map<String, String> parameters = new HashMap<>();
    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public int getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(int workThreads) {
        this.workThreads = workThreads;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
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
