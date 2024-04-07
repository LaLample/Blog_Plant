package com.lam.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lam.model.entity.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
