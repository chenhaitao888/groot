package com.groot.flow.remoting;

import java.util.List;

/**
 * @author : chenhaitao934
 * @date : 5:36 下午 2020/6/22
 */
public abstract class AbstractCommandBody {
    private String nodeId;
    private String nodeType;
    private List<String> addressList;
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
}
