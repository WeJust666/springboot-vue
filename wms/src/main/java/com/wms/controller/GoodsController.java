package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.PageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.suc():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.suc():Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Goods goods){
        return goodsService.saveOrUpdate(goods)?Result.suc():Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(Integer id){
        return goodsService.removeById(id)?Result.suc():Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParam pageParam) {
        HashMap param = pageParam.getParam();
        String name = (String) param.get("name");
        String goodstype = param.get("goodstype").toString();
        String storage = param.get("storage").toString();

        Page<Goods> page = new Page<>();
        page.setCurrent(pageParam.getPageNum());
        page.setSize(pageParam.getPageSize());

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Goods::getName, name);
        queryWrapper.eq(StringUtils.isNotBlank(goodstype), Goods::getGoodstype, goodstype);
        queryWrapper.eq(StringUtils.isNotBlank(storage), Goods::getStorage, storage);

        IPage<Goods> result = goodsService.page(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
}
