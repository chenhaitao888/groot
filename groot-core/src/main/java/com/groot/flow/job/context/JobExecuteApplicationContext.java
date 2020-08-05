package com.groot.flow.job.context;

import com.groot.flow.GrootContext;
import com.groot.flow.job.runner.GrootJobRunnerAdapter;
import com.groot.flow.job.runner.JobRunnerPool;
import com.groot.flow.remoting.GrootRemotingClientDelegate;

/**
 * @author : chenhaitao934
 * @date : 7:12 下午 2020/6/22
 */
public class JobExecuteApplicationContext extends GrootContext {
    private JobRunnerPool jobRunnerPool;
    private GrootJobRunnerAdapter runnerAdapter;
    private GrootRemotingClientDelegate remotingClientDelegate;

    public JobRunnerPool getJobRunnerPool() {
        return jobRunnerPool;
    }

    public void setJobRunnerPool(JobRunnerPool jobRunnerPool) {
        this.jobRunnerPool = jobRunnerPool;
    }

    public GrootJobRunnerAdapter getRunnerAdapter() {
        return runnerAdapter;
    }

    public void setRunnerAdapter(GrootJobRunnerAdapter runnerAdapter) {
        this.runnerAdapter = runnerAdapter;
    }

    public GrootRemotingClientDelegate getRemotingClientDelegate() {
        return remotingClientDelegate;
    }

    public void setRemotingClientDelegate(GrootRemotingClientDelegate remotingClientDelegate) {
        this.remotingClientDelegate = remotingClientDelegate;
    }
}
