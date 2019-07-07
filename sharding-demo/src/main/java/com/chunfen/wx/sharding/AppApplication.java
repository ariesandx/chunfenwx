package com.chunfen.wx.sharding;

import com.alibaba.fastjson.JSON;
import com.chunfen.wx.sharding.domain.Trade;
import com.chunfen.wx.sharding.mapper.TradeMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.chunfen.wx.sharding.mapper")
@EnableTransactionManagement
public class AppApplication implements CommandLineRunner {

    @Autowired
    TradeMapper tradeMapper;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    public void run(String... args) throws Exception {
        Trade trade = new Trade();
        trade.setShard(1);
        trade.setTid(0L);
        System.out.println(JSON.toJSONString(tradeMapper.selectOne(trade)));
    }
}