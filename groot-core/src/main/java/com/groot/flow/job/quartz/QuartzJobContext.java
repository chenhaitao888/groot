package com.groot.flow.job.quartz;

import com.groot.flow.job.context.JobContext;
import org.quartz.Trigger;

/**
 * @author : chenhaitao934
 * @date : 3:08 下午 2020/6/22
 */
public class QuartzJobContext {
    private Trigger trigger;
    private JobContext jobContext;

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public JobContext getJobContext() {
        return jobContext;
    }

    public void setJobContext(JobContext jobContext) {
        this.jobContext = jobContext;
    }
}
