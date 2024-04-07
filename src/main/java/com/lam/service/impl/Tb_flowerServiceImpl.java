package com.lam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lam.model.entity.po.Tb_flower;
import com.lam.model.mapper.Tb_flowerMapper;
import com.lam.service.Itb_flowerService;
import org.springframework.stereotype.Service;

@Service
public class Tb_flowerServiceImpl extends ServiceImpl<Tb_flowerMapper, Tb_flower> implements Itb_flowerService {
}
