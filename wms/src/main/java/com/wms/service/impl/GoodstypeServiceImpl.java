package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.entity.Goodstype;
import com.wms.mapper.GoodstypeMapper;
import com.wms.service.GoodstypeService;
import org.springframework.stereotype.Service;

@Service
public class GoodstypeServiceImpl extends ServiceImpl<GoodstypeMapper, Goodstype> implements GoodstypeService {
}
