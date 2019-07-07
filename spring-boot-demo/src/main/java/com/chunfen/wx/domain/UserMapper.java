package com.chunfen.wx.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chunfen.wx
 */
public interface UserMapper extends BaseMapper<User>{
//    xml wrapper 混用
    List<User> getAll(@Param("ew") QueryWrapper<User> queryWrapper);

    User getOne(@Param("id") int id);
}
