package com.chunfen.wx.sharding;

import com.alibaba.fastjson.JSON;
import com.chunfen.wx.sharding.domain.Trade;
import com.chunfen.wx.sharding.mapper.TradeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AppTest {

    @Autowired
    private TradeMapper tradeMapper;

    @Test
    public void test() throws Exception{
        Trade trade = new Trade();
        trade.setShard(1);
        System.out.println(JSON.toJSONString(tradeMapper.selectOne(trade)));
    }

}
