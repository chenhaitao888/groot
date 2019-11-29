package com.groot.flow.logger;

/**
 * @author chenhaitao
 * @date 2019-11-25
 */
public interface Logger {
    void debug(String message);
    void debug(Throwable e);
    void debug(String message, Throwable e);
    void debug(String format, Object... arguments);
    void info(String message);
    void info(Throwable e);
    void info(String message, Throwable e);
    void info(String format, Object... arguments);
    void warn(String message);
    void warn(Throwable e);
    public void warn(String message, Throwable e);
    void warn(String format, Object... arguments);
    void error(String message);
    void error(Throwable e);
    public void error(String message, Throwable e);
    void error(String format, Object... arguments);
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();
}
