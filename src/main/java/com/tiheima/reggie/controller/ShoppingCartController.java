package com.tiheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tiheima.reggie.common.BaseContext;
import com.tiheima.reggie.common.R;
import com.tiheima.reggie.pojo.ShoppingCart;
import com.tiheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> shoppingcartList(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);

    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        // 设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        log.info("userId -----> {}",currentId);
        // 查询是否存在
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if(dishId != null){
            // 添加的时菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else {
            // 添加套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        // 比对了菜品/套餐id 和 用户id，来确定一条数据

        ShoppingCart cartOne = shoppingCartService.getOne(queryWrapper);

        if(cartOne != null){
            // 已经存在，数量加1
            Integer number = cartOne.getNumber();
            cartOne.setNumber(number + 1);
            shoppingCartService.updateById(cartOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cartOne = shoppingCart;
        }

        return R.success(cartOne);
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();
        if(dishId != null){
            // 添加的时菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 添加套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart catOne = shoppingCartService.getOne(queryWrapper);

        if(catOne.getNumber() == 1){
            shoppingCartService.removeById(catOne);
        }else {
            catOne.setNumber(catOne.getNumber() - 1);
            shoppingCartService.updateById(catOne);
        }

        return R.success("");
    }

}
