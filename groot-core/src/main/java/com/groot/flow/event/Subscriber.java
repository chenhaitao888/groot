package com.groot.flow.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 订阅者
 */
@Setter
@Getter
@AllArgsConstructor
public class Subscriber {
    private String seriaNum;
    private Observer observer;
}
