package com.tiheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiheima.reggie.common.R;
import com.tiheima.reggie.pojo.Orders;
import com.tiheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("成功");
    }

    @GetMapping("/userPage")
    public R<Page> orderList(int page,int pageSize){
        Page<Orders> pageinfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getOrderTime);
        ordersService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);
    }
}
