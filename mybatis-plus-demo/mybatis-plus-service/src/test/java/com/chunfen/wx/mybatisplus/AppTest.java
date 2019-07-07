package com.chunfen.wx.mybatisplus;

import com.chunfen.wx.mybatisplus.mapper.TradeMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class)
public class AppTest {

    private static final Gson GSON = new Gson();

    @Autowired
    private TradeMapper tradeMapper;

    @Test
    public void test(){
        List<Map<String, Object>> maps = tradeMapper.selectListMap();
        System.out.println("list map : " + GSON.toJson(maps));

        Map<Long, Object> map = tradeMapper.selectMap();
        System.out.println("map : ");
        map.forEach((key, value) -> {
            System.out.print("key : " + GSON.toJson(key));
            System.out.println(" value : " + GSON.toJson(value));

        });
    }
}
