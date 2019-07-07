package com.chunfen.wx.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chunfen.wx.domain.User;
import com.chunfen.wx.domain.UserMapper;
import com.chunfen.wx.prop.DemoProp1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by xi.w on 2018-10-20.
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DemoProp1 demoProp1;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //@PostConstruct
    public void getuser() throws Exception{
        QueryWrapper<User> query = Wrappers.query(new User());
        query.eq("name", "tom");
        List<User> users = userMapper.selectList(query);
        System.out.println(OBJECT_MAPPER.writeValueAsString(users));
//        System.out.println(demoProp1.getAge());
//        System.out.println(demoProp1.getName());
//        System.out.println(OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(demoProp1));
    }

    //验证使用  @Valid + bindingResult
    @RequestMapping("/world")
    public User world(@Valid User user, BindingResult bindingResult) {
        System.out.println(user);
        if (bindingResult.hasErrors()) {
            for(ObjectError objectError : bindingResult.getAllErrors()){
                System.out.println(objectError.getCode() + ":" + objectError.getDefaultMessage());
            }
        }

        Map[] map = {};

        return user;
    }
}
