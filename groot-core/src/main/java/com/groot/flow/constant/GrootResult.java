package com.groot.flow.constant;

import com.groot.flow.job.JobMetaData;

/**
 * @author : chenhaitao934
 * @date : 2:11 下午 2020/6/22
 */
public class GrootResult {
    private GrootAction grootAction;
    private String msg;
    private JobMetaData jobMetaData;
    private Long completeTime;
    private boolean receiveNewTask = true;

    public GrootResult(GrootAction grootAction, String msg) {
        this.grootAction = grootAction;
        this.msg = msg;
    }

    public GrootResult(){

    }

    public GrootAction getGrootAction() {
        return grootAction;
    }

    public void setGrootAction(GrootAction grootAction) {
        this.grootAction = grootAction;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JobMetaData getJobMetaData() {
        return jobMetaData;
    }

    public void setJobMetaData(JobMetaData jobMetaData) {
        this.jobMetaData = jobMetaData;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public boolean isReceiveNewTask() {
        return receiveNewTask;
    }

    public void setReceiveNewTask(boolean receiveNewTask) {
        this.receiveNewTask = receiveNewTask;
    }
}
