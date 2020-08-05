package com.groot.flow.job.runner;

import com.groot.flow.GrootContext;
import com.groot.flow.constant.Constants;
import com.groot.flow.constant.GrootResult;
import com.groot.flow.constant.JobType;
import com.groot.flow.exception.JobExecuteException;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.job.GrootJob;
import com.groot.flow.job.JobMetaData;
import com.groot.flow.job.context.JobContext;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.job.quartz.QuartzCronJobRunnerDelegate;
import com.groot.flow.job.quartz.QuartzJobContext;
import com.groot.flow.logger.Logger;
import com.groot.flow.utils.BeanCopyUtils;
import sun.nio.ch.Interruptible;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : chenhaitao934
 * @date : 9:34 下午 2020/8/5
 */
public class GrootRunnable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(GrootRunnable.class.getName());

    private JobMetaData jobMetaData;
    private ExecuteCallback callback;
    private Interruptible interruptible;
    private JobExecuteApplicationContext context;
    private AtomicBoolean interrupted = new AtomicBoolean(false);

    public GrootRunnable(JobMetaData jobMetaData, ExecuteCallback callback, JobExecuteApplicationContext context) {
        this.jobMetaData = jobMetaData;
        this.callback = callback;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            blockedOn(interruptible);
            if (Thread.currentThread().isInterrupted()) {
                ((InterruptibleAdapter) interruptible).interrupt();
            }
            GrootResult grootResult = null;
            if (jobMetaData.getJobType() == JobType.CRON) {
                grootResult = new QuartzCronJobRunnerDelegate(buildCronJobContext(jobMetaData)).run();
                grootResult.setJobMetaData(jobMetaData);

                // 将运行结果返回给服务端 TODO
            } else if (jobMetaData.getJobType() == JobType.REPEAT) {
                // TODO

            } else if (jobMetaData.getJobType() == JobType.FLOW) {
                JobContext jobContext = buildJobContext(jobMetaData);
                GrootJobRunnerAdapter runnerAdapter = null == context.getRunnerAdapter() ? new DefaultRunnerAdapter(jobContext) : context.getRunnerAdapter();
                GrootJob jobRunner = runnerAdapter.createJobRunner();
                jobRunner.excute(jobContext);
                // TODO 实现流式任务依赖

            } else {
                logger.info("no such job type can execute");
                throw new JobExecuteException("no such job type can execute");
            }
            if (isStopToGetNewJob()) {
                grootResult.setReceiveNewTask(false);
            }
            callback.complete(grootResult);
        } finally {
            blockedOn(null);
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

    private static void blockedOn(Interruptible interruptible) {
        sun.misc.SharedSecrets.getJavaLangAccess().blockedOn(Thread.currentThread(), interruptible);
    }

    private abstract class InterruptibleAdapter implements Interruptible {

        public void interrupt(Thread thread) {
            interrupt();
        }

        public abstract void interrupt();
    }

    private boolean isStopToGetNewJob() {
        if (isInterrupted()) {
            // 如果当前线程被阻断了,那么也就不接受新任务了
            return true;
        }
        // 机器资源是否充足
        return !context.getConfig().getInternal(Constants.MACHINE_ENOUGH, true);
    }

    private boolean isInterrupted() {
        return this.interrupted.get();
    }
}
