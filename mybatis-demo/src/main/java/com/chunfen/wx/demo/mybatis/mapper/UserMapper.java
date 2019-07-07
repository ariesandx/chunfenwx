package com.chunfen.wx.demo.mybatis.mapper;

import com.chunfen.wx.demo.mybatis.domain.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author chunfen.wx
 */
public interface UserMapper {
    /*
     * 新增用戶
     * @param user
     * @return
     * @throws Exception
     */
    int insertUser(User user);

    /*
     * 修改用戶
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    int updateUser(User user);

    /*
     * 刪除用戶
     * @param id
     * @return
     * @throws Exception
     */
    int deleteUser(Integer id);

    /*
     * 根据id查询用户信息
     * @param id
     * @return
     * @throws Exception
     */
    User selectUserById(Integer id);

    /*
     * 查询所有的用户信息
     * @return
     * @throws Exception
     */
    List<User> selectAllUser();

    @MapKey("name")
    Map<String, Object> groupUser();
}
