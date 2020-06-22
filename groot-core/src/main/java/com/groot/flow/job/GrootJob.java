package com.groot.flow.job;

import com.groot.flow.constant.Constants;
import com.groot.flow.constant.GrootResult;
import com.groot.flow.job.context.JobContext;
import com.groot.flow.job.quartz.QuartzJobContext;
import org.quartz.*;

/**
 * @author : chenhaitao934
 * @date : 11:38 上午 2020/6/22
 * job interface for external implementation
 */
public interface GrootJob extends Job {
    default void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        QuartzJobContext quartzJobContext = (QuartzJobContext) jobDataMap.get(Constants.QUARTZ_CONTEXT);
        excute(quartzJobContext.getJobContext());
    }
    GrootResult excute(JobContext context);
}
