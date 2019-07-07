package test;

import com.chunfen.wx.demo.mybatis.domain.User;
import com.chunfen.wx.demo.mybatis.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author chunfen.wx
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:/spring-database.xml"})
public class SpringUserTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test(){
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        List<User> users = userMapper.selectAllUser();
        System.out.println(users);
    }
}
