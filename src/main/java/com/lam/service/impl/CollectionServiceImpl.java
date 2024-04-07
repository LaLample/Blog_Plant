package com.lam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lam.model.entity.po.CollectionBlog;
import com.lam.model.mapper.CollectionMapper;
import com.lam.service.ICollectionService;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, CollectionBlog> implements ICollectionService {
}
