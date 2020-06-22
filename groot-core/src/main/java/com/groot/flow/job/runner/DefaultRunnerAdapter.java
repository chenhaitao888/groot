package com.groot.flow.job.runner;

import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.job.GrootJob;
import com.groot.flow.job.context.JobContext;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.logger.Logger;

/**
 * @author : chenhaitao934
 * @date : 7:28 下午 2020/6/22
 */
public class DefaultRunnerAdapter implements GrootJobRunnerAdapter{
    private final Logger logger = LoggerFactory.getLogger(DefaultRunnerAdapter.class.getName());
    private JobContext context;

    public DefaultRunnerAdapter(JobContext context) {
        this.context = context;
    }

    @Override
    public GrootJob createJobRunner() {
        try {
            return context.getJobRunnerClass().newInstance();
        } catch (Exception e) {
            logger.error("create job");
        }
        return null;
    }
}
