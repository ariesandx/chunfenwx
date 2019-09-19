package com.chunfen.wx.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by xi.w on 2018-10-20.
 */
//@Component
public class DemoProp {

    //自动读取配置文件
    @Value("${demo.prop.name}")
    private String name;

    @Value("${demo.prop.age}")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
