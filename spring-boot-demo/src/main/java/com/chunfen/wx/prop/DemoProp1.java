package com.chunfen.wx.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by xi.w on 2019/5/1.
 */
@Component
@PropertySource("classpath:conf/my.properties")
@ConfigurationProperties("my")
public class DemoProp1 {

    private int age;

    private String name;

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
