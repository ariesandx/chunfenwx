package com.chunfen.wx.mybatisplus.domain;

public class Trade extends BaseDomain {

    private Long tid;

    private String title;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
