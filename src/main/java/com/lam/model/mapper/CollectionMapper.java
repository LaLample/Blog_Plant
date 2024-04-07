package com.lam.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lam.model.entity.po.Blog;
import com.lam.model.entity.po.CollectionBlog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectionMapper extends BaseMapper<CollectionBlog> {
}
