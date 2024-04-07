package com.lam.model.entity.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
@TableName("user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("uid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid ;

    @TableField("password")
    private String password;

    @TableField("state")
    private Integer state;

    @TableField("gender")
    private String gender;


    @TableField("isDelete")
    private  Integer isDelete;


    @TableField("createTime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;


}
