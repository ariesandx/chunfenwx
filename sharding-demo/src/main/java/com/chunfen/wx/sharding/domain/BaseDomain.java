package com.chunfen.wx.sharding.domain;

import java.io.Serializable;

public class BaseDomain implements Serializable {

    private int shard;

    public int getShard() {
        return shard;
    }

    public void setShard(int shard) {
        this.shard = shard;
    }
}
