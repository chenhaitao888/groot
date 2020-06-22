package com.groot.flow.job.runner;

import com.groot.flow.job.GrootJob;

/**
 * @author : chenhaitao934
 * @date : 7:17 下午 2020/6/22
 */
public interface GrootJobRunnerAdapter {

    GrootJob createJobRunner();
}
