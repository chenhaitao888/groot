package com.groot.flow.event;

import com.groot.flow.constant.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 默认的事件中心实现
 */
public class DefaultEventCenter implements EventCenter {
    private static final ConcurrentHashMap<String, Set<Subscriber>> eventMap = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(Constants.AVAILABLE_PROCESSOR * 2);

    @Override
    public void subsribe(Subscriber subscriber, String... topics) {
        Arrays.stream(topics).forEach((t) -> {
            Set<Subscriber> subscribers = eventMap.get(t);
            if (subscribers == null) {
                subscribers = new HashSet<>();
                Set<Subscriber> oSubscriber = eventMap.putIfAbsent(t, subscribers);
                if (oSubscriber != null) {
                    subscribers = oSubscriber;
                }
            }
            subscribers.add(subscriber);
        });

    }

    @Override
    public void unSubsribe(String topic, Subscriber subscriber) {
        Set<Subscriber> subscribers = eventMap.get(topic);
        if (subscriber != null) {
            subscribers.forEach((var) -> {
                if ((var.getSeriaNum().equals(subscriber.getSeriaNum()))) {
                    subscribers.remove(var);
                }
            });
        }
    }

    @Override
    public void publish(EventInfo eventInfo) {
        Set<Subscriber> subscribers = eventMap.get(eventInfo.getTopic());
        if (subscribers != null) {
            subscribers.forEach((var) -> {
                var.getObserver().handleEvent(eventInfo);
            });
        }
    }

    @Override
    public void publishSync(EventInfo eventInfo) {
        executor.submit(() -> {
            Set<Subscriber> subscribers = eventMap.get(eventInfo.getTopic());
            if (subscribers != null) {
                subscribers.forEach((var) -> {
                    var.getObserver().handleEvent(eventInfo);
                });
            }
        });

    }
}
