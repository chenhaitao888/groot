package com.groot.flow.registry;

import com.groot.flow.cluster.GrootNode;
import com.groot.flow.logger.Logger;
import com.groot.flow.factory.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public abstract class AbstractRegistry implements Registry{
    protected final static Logger logger = LoggerFactory.getLogger(Registry.class.getName());
    private final Set<GrootNode> registers = new HashSet<>();
    @Override
    public void register(GrootNode node) {
        if(node == null){
            throw new IllegalArgumentException("register node is null");
        }
        if(logger.isInfoEnabled()){
            logger.info("register node is {}", node);
        }
        registers.add(node);
        doRegister(node);
    }

    @Override
    public void unregister(GrootNode node) {

    }

    protected abstract void doRegister(GrootNode node);

    protected abstract void doUnRegister(GrootNode node);
}
