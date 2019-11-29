package com.groot.flow.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 事件
 */
@AllArgsConstructor
@Getter
@Setter
public class EventInfo {
    private String topic;
    private Map<String, Object> events;
}
