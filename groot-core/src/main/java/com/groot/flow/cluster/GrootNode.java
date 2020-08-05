package com.groot.flow.cluster;

import com.groot.flow.registry.RegistryUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenhaitao
 * @date 2019-11-27
 * 节点信息
 */
public class GrootNode {
    private String clusterName;
    private String ip;
    private Integer port;
    private String fullPath;
    private String nodeType;

    public String toFullString() {
        if (fullPath == null) {
            fullPath = RegistryUtil.getFullPath(this);
        }
        return fullPath;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getAddress() {
        return ip + ":" + port;
    }
}
