package com.groot.flow.event;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 观察接口
 */
public interface Observer {
    void onObserved(EventInfo eventInfo);
}
