package com.chunfen.wx.service;

import com.chunfen.wx.domain.User;
import com.chunfen.wx.domain.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Cacheable(cacheNames = "test", key = "'userList'")
    public List<User> getList(){
        logger.info("进缓存啦");
        return userMapper.selectList(null);
    }
}
