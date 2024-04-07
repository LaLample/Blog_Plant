package com.lam.model.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("redundancy")
@Data
public class Redundancy {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("BlogId")
    private Long blogId;

    @TableField("BlogAck")
    private Integer BlogAck;

    @TableField("isDelete")
    private Integer isDelete;

    @TableField("RedId")
    private Long RedId;

}
