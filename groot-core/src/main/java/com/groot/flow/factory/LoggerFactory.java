package com.groot.flow.factory;

import com.groot.flow.constant.GrootConfig;
import com.groot.flow.logger.Logger;
import com.groot.flow.logger.LoggerAdapter;
import com.groot.flow.spi.ServiceProviderInterface;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenhaitao
 * @date 2019-11-26
 */
public class LoggerFactory {
    private static LoggerAdapter loggerAdapter;
    private static final ConcurrentHashMap<String, Logger> map = new ConcurrentHashMap<>();
    public static Logger getLogger(String key){
        Logger logger = map.get(key);
        if(logger == null){
            map.putIfAbsent(key, loggerAdapter.getLogger(key));
            logger = map.get(key);
        }
        return logger;
    }

    public static void setLoggerAdapter(GrootConfig config) {
        try {
            setLoggerAdapter(ServiceProviderInterface.load(LoggerAdapter.class, config));
        } catch (Exception e) {
            throw new IllegalStateException("service loader fail", e);
        }
    }

    public static void setLoggerAdapter(LoggerAdapter loggerAdapter) {
        if(loggerAdapter != null){
            LoggerFactory.loggerAdapter = loggerAdapter;
            Logger logger = loggerAdapter.getLogger(LoggerFactory.class.getName());
            logger.info("logger: {}", loggerAdapter.getClass().getName());
        }
    }
}
