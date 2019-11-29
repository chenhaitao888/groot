package com.groot.flow.cluster;

import com.groot.flow.registry.RegistryUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenhaitao
 * @date 2019-11-27
 * 节点信息
 */
@Getter
@Setter
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
}
