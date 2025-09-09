package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.PageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype)?Result.suc():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype)?Result.suc():Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Goodstype goodstype){
        return goodstypeService.saveOrUpdate(goodstype)?Result.suc():Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(Integer id){
        return goodstypeService.removeById(id)?Result.suc():Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParam pageParam) {
        HashMap param = pageParam.getParam();
        String name = (String) param.get("name");

        Page<Goodstype> page = new Page<>();
        page.setCurrent(pageParam.getPageNum());
        page.setSize(pageParam.getPageSize());

        LambdaQueryWrapper<Goodstype> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Goodstype::getName, name);

        IPage<Goodstype> result = goodstypeService.page(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    @GetMapping("/list")
    public Result list() {
        return Result.suc(goodstypeService.list());
    }
}
