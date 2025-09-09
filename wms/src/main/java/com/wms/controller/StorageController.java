package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.PageParam;
import com.wms.common.Result;
import com.wms.entity.Storage;
import com.wms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage)?Result.suc():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage)?Result.suc():Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Storage storage){
        return storageService.saveOrUpdate(storage)?Result.suc():Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(Integer id){
        return storageService.removeById(id)?Result.suc():Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParam pageParam) {
        HashMap param = pageParam.getParam();
        String name = (String) param.get("name");

        Page<Storage> page = new Page<>();
        page.setCurrent(pageParam.getPageNum());
        page.setSize(pageParam.getPageSize());

        LambdaQueryWrapper<Storage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Storage::getName, name);

        IPage<Storage> result = storageService.page(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    @GetMapping("/list")
    public Result list() {
        return Result.suc(storageService.list());
    }
}
