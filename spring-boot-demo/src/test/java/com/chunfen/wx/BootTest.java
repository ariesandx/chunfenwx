package com.chunfen.wx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chunfen.wx.domain.User;
import com.chunfen.wx.domain.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xi.w on 2019/5/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BootTest {
    @Autowired
    private UserMapper userMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void selectList() throws Exception{
        QueryWrapper<User> query = Wrappers.query(new User());
        query.like("name", "tom");
        List<User> users = userMapper.selectList(query);
        System.out.println(OBJECT_MAPPER.writeValueAsString(users));
    }

    @Test
    public void insertList() throws Exception{
        User user = new User();
        user.setId(1);
        user.setName("tom_早");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test
    public void getAllList() throws Exception{
        QueryWrapper<User> query = Wrappers.query(new User());
        query.like("user_name", "tom");
        System.out.println(OBJECT_MAPPER.writeValueAsString(userMapper.getAll(query)));
    }

    @Test
    public void deleteList() throws Exception{
        QueryWrapper<User> query = Wrappers.query(new User());
        query.like("user_name", "tom");
        System.out.println(OBJECT_MAPPER.writeValueAsString(userMapper.delete(query)));
    }

    @Test
    public void getOneList() throws Exception{
        System.out.println(OBJECT_MAPPER.writeValueAsString(userMapper.getOne(3)));
    }
}
