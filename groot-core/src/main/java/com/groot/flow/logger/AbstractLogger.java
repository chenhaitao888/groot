package com.groot.flow.logger;

/**
 * @author chenhaitao
 * @date 2019-11-25
 */
public abstract class AbstractLogger implements Logger{
    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            debug(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            info(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            warn(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            error(ft.getMessage(), ft.getThrowable());
        }
    }
}
