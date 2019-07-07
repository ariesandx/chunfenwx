package com.chunfen.wx.mybatisplus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix="spring.datasource")
public class DataSourceProp {

    private String url;
    private String username;
    private String diamondCoord;
    private Integer minIdle = 1;
    private Integer maxActive = 5;

}
