package com.groot.flow.remoting;


import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.utils.SystemClock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : chenhaitao934
 * @date : 5:12 下午 2020/5/21
 */
public class GrootResponseFuture {
    private final int opaque;
    private final long timeoutMillis;
    private final AsyncCallback asyncCallback;
    private final long beginTimestamp = SystemClock.now();
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    // 保证信号量至多至少只被释放一次
    private final SemaphoreReleaseOnlyOnce once;
    // 保证回调的callback方法至多至少只被执行一次
    private final AtomicBoolean executeCallbackOnlyOnce = new AtomicBoolean(false);
    private volatile GrootCommand responseCommand;
    private volatile boolean sendRequestOK = true;
    private volatile Throwable cause;

    public GrootResponseFuture(int opaque, long timeoutMillis, AsyncCallback asyncCallback,
                          SemaphoreReleaseOnlyOnce once) {
        this.opaque = opaque;
        this.timeoutMillis = timeoutMillis;
        this.asyncCallback = asyncCallback;
        this.once = once;
    }

    public void executeInvokeCallback() {
        if (asyncCallback != null) {
            if (this.executeCallbackOnlyOnce.compareAndSet(false, true)) {
                asyncCallback.operationComplete(this);
            }
        }
    }

    public void release() {
        if (this.once != null) {
            this.once.release();
        }
    }

    public boolean isTimeout() {
        long diff = SystemClock.now() - this.beginTimestamp;
        return diff > this.timeoutMillis;
    }

    public GrootCommand waitResponse(final long timeoutMillis) throws InterruptedException {
        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return this.responseCommand;
    }

    public void putResponse(final GrootCommand responseCommand) {
        this.responseCommand = responseCommand;
        this.countDownLatch.countDown();
    }

    public long getBeginTimestamp() {
        return beginTimestamp;
    }

    public boolean isSendRequestOK() {
        return sendRequestOK;
    }

    public void setSendRequestOK(boolean sendRequestOK) {
        this.sendRequestOK = sendRequestOK;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public AsyncCallback getAsyncCallback() {
        return asyncCallback;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public GrootCommand getResponseCommand() {
        return responseCommand;
    }

    public void setResponseCommand(GrootCommand responseCommand) {
        this.responseCommand = responseCommand;
    }

    public int getOpaque() {
        return opaque;
    }

    @Override
    public String toString() {
        return "ResponseFuture [responseCommand=" + responseCommand + ", sendRequestOK=" + sendRequestOK
                + ", cause=" + cause + ", opaque=" + opaque + ", timeoutMillis=" + timeoutMillis
                + ", invokeCallback=" + asyncCallback + ", beginTimestamp=" + beginTimestamp
                + ", countDownLatch=" + countDownLatch + "]";
    }
}
