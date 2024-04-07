package com.lam.model.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@TableName("tb_flower")
@Data
public class Tb_flower {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("idolId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long idolId;

    @TableField("fansId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fansId;

    @TableField("isDelete")
    private  Integer isDelete;
}
