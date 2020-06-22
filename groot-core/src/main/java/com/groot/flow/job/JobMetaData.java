package com.groot.flow.job;

import com.groot.flow.constant.JobType;
import com.groot.flow.job.context.ShardingContext;

/**
 * @author : chenhaitao934
 * @date : 4:07 下午 2020/6/22
 */
public class JobMetaData {
    private String jobName;
    /** job当前执行的节点 */
    private String jobExecutorNode;
    private String jobId;
    private String cronExpression;
    private JobType jobType;
    private ShardingContext context;
    private Class<?> jobRunnerClass;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobExecutorNode() {
        return jobExecutorNode;
    }

    public void setJobExecutorNode(String jobExecutorNode) {
        this.jobExecutorNode = jobExecutorNode;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public ShardingContext getContext() {
        return context;
    }

    public void setContext(ShardingContext context) {
        this.context = context;
    }

    public Class<?> getJobRunnerClass() {
        return jobRunnerClass;
    }

    public void setJobRunnerClass(Class<?> jobRunnerClass) {
        this.jobRunnerClass = jobRunnerClass;
    }
}
