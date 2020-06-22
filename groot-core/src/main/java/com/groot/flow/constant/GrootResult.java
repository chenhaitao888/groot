package com.groot.flow.constant;

/**
 * @author : chenhaitao934
 * @date : 2:11 下午 2020/6/22
 */
public class GrootResult {
    private GrootAction grootAction;
    private String msg;

    public GrootResult(GrootAction grootAction, String msg) {
        this.grootAction = grootAction;
        this.msg = msg;
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
}
