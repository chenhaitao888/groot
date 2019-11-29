package com.groot.flow;

import com.groot.flow.event.DefaultEventCenter;
import com.groot.flow.event.EventInfo;
import com.groot.flow.event.Observer;
import com.groot.flow.event.Subscriber;

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

        Subscriber subscriber = new Subscriber("12", new Observer() {
            @Override
            public void handleEvent(EventInfo eventInfo) {
                System.out.println(eventInfo.getEvents());
            }
        });

        center.subsribe(subscriber, new String[]{"test", "test2"});
        center.publish(info);
        center.unSubsribe("test", subscriber);
        center.publish(info);
    }
}

