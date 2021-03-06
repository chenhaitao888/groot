package com.groot.flow.constant;

/**
 * @author chenhaitao
 * @date 2019-11-25
 * 常量
 */
public interface Constants {
    int AVAILABLE_PROCESSOR = Runtime.getRuntime().availableProcessors();
    String QUARTZ_CONTEXT = "quartz_contex";

    String MACHINE_ENOUGH = "gooot_machine_enough";

    int LATCH_TIMEOUT_MILLIS = 60 * 1000;
}
