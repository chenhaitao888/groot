package com.groot.flow.registry;

import com.groot.flow.cluster.GrootNode;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.logger.Logger;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public class RegistryUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryUtil.class.getName());
    public static String getRootPath(String clusterName) {
        return "/groot/" + clusterName + "/nodes";
    }
    public static String getFullPath(GrootNode node) {
        StringBuilder path = new StringBuilder();

        path.append(getRootPath(node.getClusterName()))
                .append("/")
                .append(node.getNodeType())
                .append("/")
                .append(node.getNodeType())
                .append(":\\\\")
                .append(node.getIp());

        if (node.getPort() != null && node.getPort() != 0) {
            path.append(":").append(node.getPort());
        }

        return path.toString();
    }

    public static String getRegistryAddress(GrootConfig config){
        String registryAddress = config.getRegistryAddress();
        if (registryAddress.startsWith("zookeeper://")) {
            return registryAddress.replace("zookeeper://", "");
        }
        throw new IllegalArgumentException("invalid registry address");
    }

    public static String getSelectorPath(String clusterName) {
        return getRootPath(clusterName) + "/selector";
    }
}

