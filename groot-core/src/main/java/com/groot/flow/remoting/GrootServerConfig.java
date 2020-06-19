package com.groot.flow.remoting;


import com.groot.flow.constant.Constants;

/**
 * @author : chenhaitao934
 * @date : 10:59 上午 2020/5/20
 */
public class GrootServerConfig {
    private int listenPort = 8888;
    private int serverWorkerThreads = 32;
    private int serverCallbackExecutorThreads;
    private int serverSelectorThreads;
    private int serverOnewaySemaphoreValue;
    private int serverAsyncSemaphoreValue;
    private int readerIdleTimeSeconds;
    private int writerIdleTimeSeconds;
    private int serverChannelMaxIdleTimeSeconds;

    public GrootServerConfig() {
        this.serverCallbackExecutorThreads = Constants.AVAILABLE_PROCESSOR * 2;
        this.serverSelectorThreads = Constants.AVAILABLE_PROCESSOR * 2;
        this.serverOnewaySemaphoreValue = 32;
        this.serverAsyncSemaphoreValue = 64;
        this.readerIdleTimeSeconds = 0;
        this.writerIdleTimeSeconds = 0;
        this.serverChannelMaxIdleTimeSeconds = 120;
    }

    public int getListenPort() {
        return this.listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getServerWorkerThreads() {
        return this.serverWorkerThreads;
    }

    public void setServerWorkerThreads(int serverWorkerThreads) {
        this.serverWorkerThreads = serverWorkerThreads;
    }

    public int getServerSelectorThreads() {
        return this.serverSelectorThreads;
    }

    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public int getServerOnewaySemaphoreValue() {
        return this.serverOnewaySemaphoreValue;
    }

    public void setServerOnewaySemaphoreValue(int serverOnewaySemaphoreValue) {
        this.serverOnewaySemaphoreValue = serverOnewaySemaphoreValue;
    }

    public int getServerCallbackExecutorThreads() {
        return this.serverCallbackExecutorThreads;
    }

    public void setServerCallbackExecutorThreads(int serverCallbackExecutorThreads) {
        this.serverCallbackExecutorThreads = serverCallbackExecutorThreads;
    }

    public int getServerAsyncSemaphoreValue() {
        return this.serverAsyncSemaphoreValue;
    }

    public void setServerAsyncSemaphoreValue(int serverAsyncSemaphoreValue) {
        this.serverAsyncSemaphoreValue = serverAsyncSemaphoreValue;
    }

    public int getServerChannelMaxIdleTimeSeconds() {
        return this.serverChannelMaxIdleTimeSeconds;
    }

    public void setServerChannelMaxIdleTimeSeconds(int serverChannelMaxIdleTimeSeconds) {
        this.serverChannelMaxIdleTimeSeconds = serverChannelMaxIdleTimeSeconds;
    }

    public int getReaderIdleTimeSeconds() {
        return this.readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return this.writerIdleTimeSeconds;
    }

    public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
    }
}
