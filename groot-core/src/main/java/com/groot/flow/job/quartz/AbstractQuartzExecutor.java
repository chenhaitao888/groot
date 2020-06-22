package com.groot.flow.job.quartz;

import com.groot.flow.constant.Constants;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

/**
 * @author : chenhaitao934
 * @date : 3:57 下午 2020/6/22
 */
public abstract class AbstractQuartzExecutor {
    protected QuartzJobContext context;

    protected JobDetail createJobDetail(){
        JobDetail jobDetail = JobBuilder.newJob(context.getJobContext().getJobRunnerClass())
                .withIdentity(context.getJobContext().getJobName()).build();
        jobDetail.getJobDataMap().put(Constants.QUARTZ_CONTEXT, context);
        return jobDetail;
    }

    protected abstract Trigger createTrigger();

    protected abstract Scheduler createScheduler();
}
