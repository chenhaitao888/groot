package com.groot.flow.event;


import com.groot.flow.factory.NamedThreadFactory;

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
    private final ConcurrentHashMap<String, Set<EventSubscriber>> ecMap =
            new ConcurrentHashMap<String, Set<EventSubscriber>>();

    private final ExecutorService executor = Executors.newFixedThreadPool(4 * 2, new NamedThreadFactory("GootEventCenter-Executor", true));
    public void subscribe(EventSubscriber subscriber, String... topics) {
        for (String topic : topics) {
            Set<EventSubscriber> subscribers = ecMap.get(topic);
            if (subscribers == null) {
                subscribers = new ConcurrentHashSet<EventSubscriber>();
                Set<EventSubscriber> oldSubscribers = ecMap.putIfAbsent(topic, subscribers);
                if (oldSubscribers != null) {
                    subscribers = oldSubscribers;
                }
            }
            subscribers.add(subscriber);
        }
    }

    public void unSubscribe(String topic, EventSubscriber subscriber) {
        Set<EventSubscriber> subscribers = ecMap.get(topic);
        if (subscribers != null) {
            for (EventSubscriber eventSubscriber : subscribers) {
                if (eventSubscriber.getId().equals(subscriber.getId())) {
                    subscribers.remove(eventSubscriber);
                }
            }
        }
    }

    public void publishSync(EventInfo eventInfo) {
        Set<EventSubscriber> subscribers = ecMap.get(eventInfo.getTopic());
        if (subscribers != null) {
            for (EventSubscriber subscriber : subscribers) {
                eventInfo.setTopic(eventInfo.getTopic());
                try {
                    //subscriber.getObserver().onObserved(eventInfo);
                    subscriber.getConsumer().accept(eventInfo);
                } catch (Throwable t) {      // 防御性容错

                }
            }
        }
    }

    public void publishAsync(final EventInfo eventInfo) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                String topic = eventInfo.getTopic();

                Set<EventSubscriber> subscribers = ecMap.get(topic);
                if (subscribers != null) {
                    for (EventSubscriber subscriber : subscribers) {
                        try {
                            eventInfo.setTopic(topic);
                            subscriber.getObserver().onObserved(eventInfo);
                        } catch (Throwable t) {     // 防御性容错

                        }
                    }
                }
            }
        });
    }
}
