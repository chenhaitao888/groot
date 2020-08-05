package com.groot.flow;


import com.groot.flow.concurrent.CustomizeThreadPollExecutor;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.event.DefaultEventCenter;
import com.groot.flow.exception.RemotingException;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.job.runner.JobRunnerPool;
import com.groot.flow.netty.client.GrootRemotingClient;
import com.groot.flow.processor.JobHandlerProcessor;
import com.groot.flow.remoting.GrootClient;
import com.groot.flow.remoting.GrootClientConfig;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.utils.NetUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : chenhaitao934
 * @date : 5:51 下午 2020/5/21
 */
public class BootStrapClient {
    public static void main(String[] args) throws Exception {
        String confPath = "/Users/chenhaitao/IdeaProjects/groot/groot-startup/src/main/resources";
        String log4jPath = confPath + "/log4j.properties";
        String serverPath = confPath + "/conf/groot-server.yml";
        PropertyConfigurator.configure(log4jPath);
        ServerConfig serverConfig = new ServerConfig();
        File file = new File(serverPath);
        FileInputStream in = new FileInputStream(file);
        serverConfig = serverConfig.fromYAML(in);
        LoggerFactory.setLoggerAdapter(serverConfig.getGrootConfig());
        CustomizeThreadPollExecutor executor = new CustomizeThreadPollExecutor("client thread", 50, 50, 60, 2000,
                false, null);
        ExecutorService executorService = executor.initializeExecutor(executor, new ThreadPoolExecutor.AbortPolicy());
        GrootClientConfig clientConfig = new GrootClientConfig();
        GrootClient client = new GrootRemotingClient(clientConfig);
        client.start();
        JobExecuteApplicationContext applicationContext = new JobExecuteApplicationContext();
        DefaultEventCenter eventCenter = new DefaultEventCenter();
        applicationContext.setEventCenter(eventCenter);
        GrootConfig config = new GrootConfig();
        config.setId("testQuartz");
        config.setWorkThreads(10);
        applicationContext.setConfig(config);
        JobRunnerPool runnerPool = new JobRunnerPool(applicationContext);
        applicationContext.setJobRunnerPool(runnerPool);
        client.registerProcessor(1, new JobHandlerProcessor(applicationContext), executorService);
        GrootCommand command = new GrootCommand();
        command.setCode(2);
        command.setOpaque(2);
        command.setRemark("hello server..");
        client.invokeSync(NetUtils.getLocalHost() +":8888", command, 100 * 60);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.shutdown();
            } catch (RemotingException e) {
                e.printStackTrace();
            }
        }));
    }
}
