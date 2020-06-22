package com.groot.flow.processor;

import com.groot.flow.GrootContext;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.logger.Logger;

/**
 * @author : chenhaitao934
 * @date : 6:51 下午 2020/6/22
 */
public abstract class AbstractProcessor {

    public Logger logger = LoggerFactory.getLogger(AbstractProcessor.class.getName());
    protected GrootContext context;

    public AbstractProcessor(GrootContext context) {
        this.context = context;
    }
}
