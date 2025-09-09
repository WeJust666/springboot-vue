package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.PageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    //查询账号
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List list = userService.lambdaQuery().eq(User::getNo, no).list();
        return list().size()>0?Result.suc(list):Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParam pageParam) {
        HashMap param = pageParam.getParam();
        String name = (String) param.get("name");
        String sex = param.get("sex").toString();
        String roleId = param.get("roleId").toString();

        Page<User> page = new Page<>();
        page.setCurrent(pageParam.getPageNum());
        page.setSize(pageParam.getPageSize());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), User::getName, name);
        queryWrapper.eq(StringUtils.isNotBlank(sex), User::getSex, sex);
        queryWrapper.eq(StringUtils.isNotBlank(roleId), User::getRoleId, roleId);

        IPage<User> result = userService.page(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    @PostMapping("/login")
    public Result Login(@RequestBody User user) {
        List list = userService.lambdaQuery()
                .eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword()).list();
        if (list.size()>0){
            User u = (User) list.get(0);
            List munuList = menuService.lambdaQuery().like(Menu::getMenuright, u.getRoleId()).list();
            HashMap map = new HashMap();
            map.put("user", u);
            map.put("menu", munuList);
            return Result.suc(map);
        }else{
            return Result.fail();
        }
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user) ? Result.suc() : Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateById(user) ? Result.suc() : Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody User user) {
        return userService.saveOrUpdate(user) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(Integer id) {
        return userService.removeById(id) ? Result.suc() : Result.fail();
    }

    //模糊查询
    @PostMapping("/search")
    public Result search(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(user.getName()), User::getName, user.getName());

        return Result.suc(userService.list(queryWrapper));
    }
}
