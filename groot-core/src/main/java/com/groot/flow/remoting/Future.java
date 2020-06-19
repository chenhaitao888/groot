package com.groot.flow.remoting;

/**
 * @author : chenhaitao934
 * @date : 3:29 下午 2020/5/20
 */
public interface Future {
    boolean isSuccess();
    Throwable cause();
}
