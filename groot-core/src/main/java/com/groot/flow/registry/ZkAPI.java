package com.groot.flow.registry;

import org.apache.curator.framework.recipes.leader.LeaderSelector;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public interface ZkAPI {
    void createNode(String path, Object data, boolean ephemeral, boolean sequential);
    void deleteNode(String path);
    <T> T getData(String path, Class<T> clazz);
    void setData(String path, Object data);
    boolean exists(String path);
    LeaderSelector getSelector();
}
