package com.chunfen.wx.mybatisplus.mapper;

import com.chunfen.wx.mybatisplus.domain.Trade;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface TradeMapper{

    List<Trade> selectAll();

    @MapKey("tid")
    Map<Long, Object> selectMap();

    List<Map<String, Object>> selectListMap();

}
