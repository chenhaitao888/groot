package com.groot.flow;

import com.groot.flow.event.DefaultEventCenter;
import com.groot.flow.event.EventInfo;
import com.groot.flow.event.EventSubscriber;
import com.groot.flow.event.Observer;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Map<String, Object> param = new HashMap<>();
        param.put("呵呵","呵呵");
        EventInfo info = new EventInfo("test", param);
        DefaultEventCenter center = new DefaultEventCenter();

        EventSubscriber subscriber = new EventSubscriber("12", (eventInfo) -> {
                System.out.println(eventInfo.getEvents());
        });

        center.subscribe(subscriber, new String[]{"test", "test2"});
        center.publishSync(info);
        center.unSubscribe("test", subscriber);
        center.publishSync(info);
    }
}

