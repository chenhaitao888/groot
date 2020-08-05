package com.groot.flow;


import com.groot.flow.concurrent.CustomizeThreadPollExecutor;
import com.groot.flow.constant.JobType;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.job.JobMetaData;
import com.groot.flow.job.runner.Test;
import com.groot.flow.netty.server.GrootRemotingServer;
import com.groot.flow.processor.ServerProcessor;
import com.groot.flow.remoting.GrootServer;
import com.groot.flow.remoting.command.JobPushRequest;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.remoting.GrootServerConfig;
import com.groot.flow.remoting.channel.GrootChannel;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : chenhaitao934
 * @date : 5:50 下午 2020/5/21
 */
public class BootStrapServer {

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
        CustomizeThreadPollExecutor executor = new CustomizeThreadPollExecutor("server thread", 50, 50, 60, 2000,
                false, null);
        ExecutorService executorService = executor.initializeExecutor(executor, new ThreadPoolExecutor.AbortPolicy());
        GrootServerConfig config = new GrootServerConfig();
        GrootServer server = new GrootRemotingServer(config);
        server.start();
        server.registerProcessor(2, new ServerProcessor(), executorService);
        while (true) {
            Thread.sleep(5000);
            GrootCommand command = new GrootCommand();
            command.setCode(1);
            JobPushRequest body = new JobPushRequest();
            body.setClassName("com.groot.flow.processor.JobExcutor");
            body.setMethodName("testJob");
            body.setJobName("fistTestJob");
            List<String> list = new ArrayList<>();
            list.add("172.16.210.173");
            body.setAddressList(list);
            JobMetaData jobMetaData = new JobMetaData();
            jobMetaData.setCronExpression("0/10 * * * * ?");
            jobMetaData.setJobName("testquartz");
            jobMetaData.setJobRunnerClass(Test.class);
            jobMetaData.setJobType(JobType.CRON);
            List<JobMetaData> jobMetaDataList = new ArrayList<>();
            jobMetaDataList.add(jobMetaData);
            body.setJobMetaDataList(jobMetaDataList);
            command.setBody(body);
            command.setOpaque(1);
            GrootChannel channel1 = server.getChannel(list);
            if (channel1 != null) {
                System.out.println();
                server.invokeSync(channel1, command, 100 * 60);
            }

        }
        /*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.shutdown();
                } catch (RemotingException e) {
                    e.printStackTrace();
                }
            }
        }));*/
    }
}
