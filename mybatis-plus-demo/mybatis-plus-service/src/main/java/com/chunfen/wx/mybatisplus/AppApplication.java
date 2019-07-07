package com.chunfen.wx.mybatisplus;


import com.chunfen.wx.mybatisplus.config.DataSourceProp;
import com.chunfen.wx.mybatisplus.config.DubboProp;
import com.chunfen.wx.mybatisplus.mapper.TradeMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.chunfen.wx.mybatisplus.mapper")
@EnableConfigurationProperties({DataSourceProp.class, DubboProp.class})
public class AppApplication {

    @Autowired
    TradeMapper tradeMapper;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
        System.out.println("running");
    }
}