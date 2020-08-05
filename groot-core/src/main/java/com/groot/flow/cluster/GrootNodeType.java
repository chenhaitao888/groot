package com.groot.flow.cluster;

import com.groot.flow.utils.StringUtils;

/**
 * @author : chenhaitao934
 * @date : 9:13 下午 2020/8/5
 */
public enum GrootNodeType {
    GROOT_DISPENSE,
    GROOT_EXECUTOR,
    GROOT_MONITOR;

    public static GrootNodeType convert(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return GrootNodeType.valueOf(value);
    }
}
