package com.groot.flow.remoting;

/**
 * @author : chenhaitao934
 * @date : 5:35 下午 2020/5/21
 */
public interface AsyncCallback {
    void operationComplete(final ResponseFuture responseFuture);
}
