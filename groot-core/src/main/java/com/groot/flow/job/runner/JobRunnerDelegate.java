package com.groot.flow.job.runner;

import com.groot.flow.constant.GrootResult;

/**
 * @author : chenhaitao934
 * @date : 4:01 下午 2020/6/22
 */
public interface JobRunnerDelegate {
    GrootResult run();
}
