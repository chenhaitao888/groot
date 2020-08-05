package com.groot.flow.remoting.command;

import java.util.List;

/**
 * @author : chenhaitao934
 * @date : 5:36 下午 2020/6/22
 */
public abstract class AbstractCommandBody implements GrootCommandBody {
    private String nodeId;
    private String nodeType;
    private List<String> addressList;
    private String identity;
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
