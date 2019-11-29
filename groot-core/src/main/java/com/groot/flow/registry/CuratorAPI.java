package com.groot.flow.registry;

import com.groot.flow.constant.EXGrootConfig;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.factory.LoggerFactory;
import com.groot.flow.logger.Logger;
import com.groot.flow.serializable.SerializableAdapter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;

/**
 * @author chenhaitao
 * @date 2019-11-27
 */
public class CuratorAPI extends AbstractZkAPI {
    private static final Logger logger = LoggerFactory.getLogger(CuratorAPI.class.getName());
    private final CuratorFramework client;
    private static final int SESSION_TIMEOUT_MS = Integer.getInteger("curator-default-session-timeout", 5000);
    private static final int CONNECTION_TIMEOUT_MS = Integer.getInteger("curator-default-connection-timeout", 5000);
    private final LeaderSelector leaderSelector;

    public CuratorAPI(GrootConfig config) {
        this.client = CuratorFrameworkFactory.builder().connectString(RegistryUtil.getRegistryAddress(config))
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                .build();
        this.client.start();
        this.leaderSelector = new LeaderSelector(client, RegistryUtil.getSelectorPath(config.getClusterName()), new LeaderSelectorListenerAdapter() {
            private Object mutex = new Object();
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                String hostAddress = InetAddress.getLocalHost().getHostAddress();
                logger.info("{} is leader", hostAddress);
                // 具体业务逻辑 TODO
                try {
                    synchronized (mutex) {
                        mutex.wait();
                    }
                } catch (InterruptedException e) {
                    logger.info("{} has been interrupted", hostAddress);
                }
            }
        });
        this.leaderSelector.autoRequeue();
    }

    @Override
    protected void createPersistentNode(String path, Object data, boolean sequential) {
        try {
            if (sequential) {
                if (data == null) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                            .forPath(path);
                } else {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                            .forPath(path, SerializableAdapter.serialize(data));
                }
            }else {
                if (data == null) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                            .forPath(path);
                } else {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                            .forPath(path, SerializableAdapter.serialize(data));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected void creatEphemeralNode(String path, Object data, boolean sequential) {
        try {
            if (sequential) {
                if (data == null) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                            .forPath(path);
                } else {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                            .forPath(path, SerializableAdapter.serialize(data));
                }
            }else {
                if (data == null) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                            .forPath(path);
                } else {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                            .forPath(path, SerializableAdapter.serialize(data));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteNode(String path) {
        try {
            this.client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T getData(String path, Class<T> clazz) {
        try {
            T data = SerializableAdapter.deserialize(this.client.getData().forPath(path), clazz);
            return data;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void setData(String path, Object data) {

    }

    @Override
    public boolean exists(String path) {
        assert client != null;
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public LeaderSelector getSelector() {
        return this.leaderSelector;
    }
}
