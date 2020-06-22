package com.groot.flow.job.runner;

import com.groot.flow.concurrent.CustomizeThreadPollExecutor;
import com.groot.flow.constant.EcTopic;
import com.groot.flow.constant.GrootResult;
import com.groot.flow.constant.JobType;
import com.groot.flow.event.EventInfo;
import com.groot.flow.event.EventSubscriber;
import com.groot.flow.event.Observer;
import com.groot.flow.exception.JobExecuteException;
import com.groot.flow.exception.NoAvailableWorkThreadException;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.job.GrootJob;
import com.groot.flow.job.JobMetaData;
import com.groot.flow.job.context.JobContext;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.job.quartz.QuartzCronJobRunnerDelegate;
import com.groot.flow.job.quartz.QuartzJobContext;
import com.groot.flow.logger.Logger;
import com.groot.flow.processor.JobHandlerProcessor;
import com.groot.flow.utils.BeanCopyUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : chenhaitao934
 * @date : 7:14 下午 2020/6/22
 */
public class JobRunnerPool {
    private final Logger logger = LoggerFactory.getLogger(JobRunnerPool.class.getName());
    private JobExecuteApplicationContext context;
    private ThreadPoolExecutor threadPoolExecutor;
    public JobRunnerPool(JobExecuteApplicationContext context) {
        this.context = context;
        context.getEventCenter().subscribe(new EventSubscriber(context.getConfig().getId(), (eventInfo) -> {
            setWorkThread(context.getConfig().getWorkThreads());
        }), EcTopic.WORK_THREAD_CHANGE);
        CustomizeThreadPollExecutor executor = new CustomizeThreadPollExecutor("JobRunnerPoll", context.getConfig().getWorkThreads(),
                context.getConfig().getWorkThreads(), 30, context.getConfig().getQueueCapacity(),
                false, null);
        threadPoolExecutor = (ThreadPoolExecutor) executor.initializeExecutor(executor, new ThreadPoolExecutor.AbortPolicy());
    }

    public void setWorkThread(int workThread) {
        if (workThread == 0) {
            throw new IllegalArgumentException("workThread can not be zero!");
        }

        threadPoolExecutor.setMaximumPoolSize(workThread);
        threadPoolExecutor.setCorePoolSize(workThread);
        logger.info("workThread update to {}", workThread);
    }

    public int getAvailablePoolSize() {
        return threadPoolExecutor.getMaximumPoolSize() - threadPoolExecutor.getActiveCount();
    }

    public void execute(JobMetaData jobMetaData, ExecuteCallback callback) throws NoAvailableWorkThreadException{
        try {
            threadPoolExecutor.execute(() -> {
                if(jobMetaData.getJobType() == JobType.CRON){
                    GrootResult run = new QuartzCronJobRunnerDelegate(buildCronJobContext(jobMetaData)).run();
                    // 将运行结果返回给服务端 TODO
                }
                if(jobMetaData.getJobType() == JobType.REPEAT){
                    // TODO

                }
                if(jobMetaData.getJobType() == JobType.FLOW){
                    JobContext jobContext = buildJobContext(jobMetaData);
                    GrootJobRunnerAdapter runnerAdapter = null == context.getRunnerAdapter() ? new DefaultRunnerAdapter(jobContext) : context.getRunnerAdapter();
                    GrootJob jobRunner = runnerAdapter.createJobRunner();
                    jobRunner.excute(jobContext);
                    // TODO 实现流式任务依赖

                }else {
                    logger.info("no such job type can execute");
                    throw new JobExecuteException("no such job type can execute");
                }
            });
        } catch (RejectedExecutionException e) {
            logger.error("no available work thread to run job");
            throw new NoAvailableWorkThreadException(e);
        }
    }

    private QuartzJobContext buildCronJobContext(JobMetaData jobMetaData) {
        QuartzJobContext context = new QuartzJobContext();
        JobContext jobContext = buildJobContext(jobMetaData);
        context.setJobContext(jobContext);
        return context;
    }

    private JobContext buildJobContext(JobMetaData jobMetaData){
        JobContext jobContext = new JobContext();
        BeanCopyUtils.copy(jobMetaData, jobContext);
        return jobContext;
    }
}
