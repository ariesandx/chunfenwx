package com.chunfen.wx.mybatisplus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix="spring.dubbo")
public class DubboProp {

    private boolean init;
    private boolean check;
    private DbInfoRequest dbInfoRequest;

    private String zkAddress;
    private String applicationName;

    @Setter
    @Getter
    public static class DbInfoRequest{
        private String version;
    }
}
