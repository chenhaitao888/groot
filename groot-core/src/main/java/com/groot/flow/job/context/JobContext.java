package com.groot.flow.job.context;

import com.groot.flow.constant.JobType;
import com.groot.flow.job.GrootJob;

/**
 * @author : chenhaitao934
 * @date : 1:47 下午 2020/6/22
 */
public class JobContext {
    private String jobName;
    private String jobId;
    private ShardingContext context;
    private String cronExpression;
    private JobType jobType;
    private Class<? extends GrootJob> jobRunnerClass;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public ShardingContext getContext() {
        return context;
    }

    public void setContext(ShardingContext context) {
        this.context = context;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Class<? extends GrootJob> getJobRunnerClass() {
        return jobRunnerClass;
    }

    public void setJobRunnerClass(Class<? extends GrootJob> jobRunnerClass) {
        this.jobRunnerClass = jobRunnerClass;
    }
}
