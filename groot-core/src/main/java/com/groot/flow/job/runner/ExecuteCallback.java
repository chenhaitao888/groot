package com.groot.flow.job.runner;

import com.groot.flow.constant.GrootResult;
import com.groot.flow.job.JobMetaData;

/**
 * @author : chenhaitao934
 * @date : 7:02 下午 2020/6/22
 */
public interface ExecuteCallback {

    JobMetaData complete(GrootResult result);
}
