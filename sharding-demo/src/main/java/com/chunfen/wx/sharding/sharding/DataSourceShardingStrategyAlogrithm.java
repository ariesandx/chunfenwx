package com.chunfen.wx.sharding.sharding;

import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataSourceShardingStrategyAlogrithm implements HintShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        String shard = shardingValue.getColumnName();
        List<String> dsColl = new ArrayList<>();
        for(String target : availableTargetNames){
            if(target.contains(shard)){
                dsColl.add(target);
            }
        }
        return null;
    }
}
