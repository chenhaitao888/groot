package com.groot.flow.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 订阅者
 */

public class EventSubscriber {
    private String id;
    private Observer observer;
    private Consumer<EventInfo> consumer;

    public EventSubscriber(String id, Consumer<EventInfo> consumer) {
        this.id = id;
        this.consumer = consumer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Consumer<EventInfo> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<EventInfo> consumer) {
        this.consumer = consumer;
    }
}
