package com.groot.flow.job.runner;

import com.groot.flow.constant.GrootResult;
import com.groot.flow.job.context.JobContext;

/**
 * @author : chenhaitao934
 * @date : 4:01 下午 2020/6/22
 */
public interface JobRunnerDelegate {
    GrootResult run();
}
