package com.groot.flow.registry;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public abstract class AbstractZkAPI implements ZkAPI {
    @Override
    public void createNode(String path, Object data, boolean ephemeral, boolean sequential) {
        /*int i = path.lastIndexOf('/');
        if (i > 0) {
            createNode(path.substring(0, i), data, false, false);
        }*/
        if(ephemeral){
            creatEphemeralNode(path, data, sequential);
        }else {
            createPersistentNode(path, data, sequential);
        }
    }

    protected abstract void createPersistentNode(String path, Object data, boolean sequential);

    protected abstract void creatEphemeralNode(String path, Object data, boolean sequential);
}
