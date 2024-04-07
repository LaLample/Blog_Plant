package com.lam.model.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@TableName("collection")
public class CollectionBlog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("useId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long useId;

    @TableField("blogId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long blogId;

    @TableField("isDelete")
    private Integer isDelete;
}
