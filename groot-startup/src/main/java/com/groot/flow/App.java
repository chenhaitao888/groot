package com.groot.flow;

import com.groot.flow.cluster.GrootNode;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.factory.ZkFactory;
import com.groot.flow.logger.Logger;
import com.groot.flow.registry.Registry;
import com.groot.flow.registry.RegistryUtil;
import com.groot.flow.registry.ZkRegistry;
import com.groot.flow.utils.NetUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        String confPath = "/Users/chenhaitao/Downloads/groot-parent/groot-startup/src/main/resources/conf";
        String log4jPath = confPath + "/log4j.properties";
        String serverPath = confPath + "/groot-server.yml";
        ServerConfig serverConfig = new ServerConfig();
        File file = new File(serverPath);
        FileInputStream in = new FileInputStream(file);
        serverConfig = serverConfig.fromYAML(in);
        GrootContext context = new GrootContext();
        context.setConfig(serverConfig.getGrootConfig());
        PropertyConfigurator.configure(log4jPath);
        LoggerFactory.setLoggerAdapter(serverConfig.getGrootConfig());
        Logger logger = LoggerFactory.getLogger(RegistryUtil.class.getName());
        logger.info("测试 [{},{}]", "哈哈","爱你");
        logger.info(serverConfig.getGrootConfig().toString());
        GrootNode node =  new GrootNode();
        node.setClusterName(serverConfig.getGrootConfig().getClusterName());
        node.setIp(NetUtils.getLocalHost());
        node.setPort(serverConfig.getGrootConfig().getListenPort());
        node.setNodeType("GROOT_SERVER");
        ZkFactory.setZkAdapter(serverConfig.getGrootConfig());
        Registry registry = new ZkRegistry(context);
        registry.register(node);
        for(;;);
    }
}
