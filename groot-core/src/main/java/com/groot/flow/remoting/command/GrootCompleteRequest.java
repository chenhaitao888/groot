package com.groot.flow.remoting.command;

import com.groot.flow.constant.GrootResult;
import com.groot.flow.remoting.command.AbstractCommandBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : chenhaitao934
 * @date : 9:00 下午 2020/8/5
 */
public class GrootCompleteRequest extends AbstractCommandBody {

    private boolean receiveNewTask;

    private List<GrootResult> grootResults;

    public boolean isReceiveNewTask() {
        return receiveNewTask;
    }

    public void setReceiveNewTask(boolean receiveNewTask) {
        this.receiveNewTask = receiveNewTask;
    }

    public List<GrootResult> getGrootResults() {
        return grootResults;
    }

    public void setGrootResults(List<GrootResult> grootResults) {
        this.grootResults = grootResults;
    }


    public void addGrootResults(GrootResult grootResult){
        if(grootResults == null){
            grootResults = new ArrayList<>();
        }
        grootResults.add(grootResult);
    }
}
