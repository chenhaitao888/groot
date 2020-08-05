package com.groot.flow.remoting.command;

import com.groot.flow.remoting.command.AbstractCommandBody;
import com.groot.flow.remoting.command.GrootCommandBody;

import java.util.List;

/**
 * @author : chenhaitao934
 * @date : 11:55 下午 2020/6/22
 */
public class JobRunResponse extends AbstractCommandBody implements GrootCommandBody {
    private List<String> failureJobId;

    public List<String> getFailureJobId() {
        return failureJobId;
    }

    public void setFailureJobId(List<String> failureJobId) {
        this.failureJobId = failureJobId;
    }
}
