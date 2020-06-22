package com.groot.flow.job.quartz;

import com.groot.flow.constant.GrootAction;
import com.groot.flow.constant.GrootResult;
import com.groot.flow.exception.JobExecuteException;
import com.groot.flow.job.context.JobContext;
import com.groot.flow.job.runner.JobRunnerDelegate;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * @author : chenhaitao934
 * @date : 4:14 下午 2020/6/22
 */
public class QuartzCronJobRunnerDelegate extends AbstractQuartzExecutor implements JobRunnerDelegate {

    public QuartzCronJobRunnerDelegate(QuartzJobContext context) {
        this.context = context;
    }

    @Override
    protected Trigger createTrigger() {
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(context.getJobContext().getJobName())
                .withSchedule(CronScheduleBuilder.cronSchedule(context.getJobContext().getCronExpression())
                .withMisfireHandlingInstructionDoNothing()).build();
        return cronTrigger;
    }

    @Override
    protected Scheduler createScheduler() {
        Scheduler scheduler;
        StdSchedulerFactory factory = new StdSchedulerFactory();
        try {
            scheduler = factory.getScheduler();
        } catch (SchedulerException e) {
            throw new JobExecuteException(e);
        }
        return scheduler;
    }

    @Override
    public GrootResult run() {
        Scheduler scheduler = createScheduler();
        final JobDetail jobDetail = createJobDetail();
        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, createTrigger());
            }
            scheduler.start();
            return new GrootResult(GrootAction.SUCCESS, "excute job success");
        } catch (SchedulerException e) {
            throw new JobExecuteException(e);
        }
    }
}
