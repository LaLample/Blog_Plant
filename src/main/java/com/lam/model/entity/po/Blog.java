package com.lam.model.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@TableName("blog")
public class Blog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("Title")
    private String Title;

    @TableField("Detail")
    private String Detail;

    @TableField("txtId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long txtId;

    @TableField("isDelete")
    private Integer isDelete;

    @TableField("uid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    @TableField("isLike")
    private Long isLike;

    @TableField("collection")
    private Long collection;
}
