package com.chunfen.wx.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by xi.w on 2018-10-20.
 */
@TableName("user")
public class User implements Serializable{

    //验证用户属性， 后端验证 结合 bindingResult
    @NotEmpty
    @TableField("user_name")
    private String name;

    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
