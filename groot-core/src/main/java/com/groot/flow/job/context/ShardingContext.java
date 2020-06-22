package com.groot.flow.job.context;

/**
 * @author : chenhaitao934
 * @date : 1:54 下午 2020/6/22
 */
public class ShardingContext {
    private int shardingTotalCount;
    private String shardingItemParameters;
    private int shardingItem;

    public ShardingContext(int shardingTotalCount, String shardingItemParameters, int shardingItem) {
        this.shardingTotalCount = shardingTotalCount;
        this.shardingItemParameters = shardingItemParameters;
        this.shardingItem = shardingItem;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public int getShardingItem() {
        return shardingItem;
    }

    public void setShardingItem(int shardingItem) {
        this.shardingItem = shardingItem;
    }
}
