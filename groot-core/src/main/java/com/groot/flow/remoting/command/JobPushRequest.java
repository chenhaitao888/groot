package com.groot.flow.remoting.command;

import com.groot.flow.job.JobMetaData;

import java.util.List;

/**
 * @author : chenhaitao934
 * @date : 7:22 下午 2020/5/22
 */
public class JobPushRequest extends AbstractCommandBody implements GrootCommandBody {
    private String jobName;
    private String className;
    private String methodName;
    private List<JobMetaData> jobMetaDataList;
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<JobMetaData> getJobMetaDataList() {
        return jobMetaDataList;
    }

    public void setJobMetaDataList(List<JobMetaData> jobMetaDataList) {
        this.jobMetaDataList = jobMetaDataList;
    }
}
