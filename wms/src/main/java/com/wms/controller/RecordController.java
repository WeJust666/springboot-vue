package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.PageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private GoodsService goodsService;

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParam pageParam) {
        HashMap param = pageParam.getParam();
        String name = (String) param.get("name");
        String goodstype = param.get("goodstype").toString();
        String storage = param.get("storage").toString();
        String roleId = param.get("roleId").toString();
        String userId = param.get("userId").toString();

        Page<Record> page = new Page<>();
        page.setCurrent(pageParam.getPageNum());
        page.setSize(pageParam.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("a.goods = b.id and b.storage = c.id and b.goodsType = d.id");

        if("2".equals(roleId)){
            queryWrapper.eq("a.userId", userId);
        }
        queryWrapper.like(StringUtils.isNotBlank(name), "b.name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goodstype), "d.id", goodstype);
        queryWrapper.eq(StringUtils.isNotBlank(storage), "c.id", storage);

        IPage result = recordService.listPage(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getCount();
        //出库
        if("2".equals(record.getAction())){
            n = -n;
            record.setCount(n);
        }
        int num = n + goods.getCount();
        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record)?Result.suc():Result.fail();
    }

}
