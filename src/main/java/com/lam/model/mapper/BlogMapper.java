package com.lam.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lam.model.entity.po.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {


    void addLike(@Param("blogId") Long blogId);

    void deleteLike(@Param("blogId")Long blogId);

    List<Blog> getHot();
}
