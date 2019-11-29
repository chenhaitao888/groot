package com.groot.flow.factory;

import com.groot.flow.constant.GrootConfig;
import com.groot.flow.registry.ZkAPI;
import com.groot.flow.registry.ZkAdapter;
import com.groot.flow.spi.ServiceProviderInterface;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenhaitao
 * @date 2019-11-27
 * zk工厂
 */
public class ZkFactory {
    private static ZkAdapter zkAdapter;
    private static final ConcurrentHashMap<String, ZkAPI> zk = new ConcurrentHashMap<>();
    public static ZkAPI getZkAPI(GrootConfig config) {
        return zkAdapter.getZkAPI(config);
    }
    public static void setZkAdapter(GrootConfig config){
        try {
            ZkAdapter adapter = ServiceProviderInterface.load(ZkAdapter.class, config);
            if(adapter != null){
                zkAdapter = adapter;
            }
        } catch (Exception e) {
            throw new IllegalStateException("service loader fail", e);
        }
    }
}
