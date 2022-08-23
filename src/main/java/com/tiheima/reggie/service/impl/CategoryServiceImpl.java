package com.tiheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiheima.reggie.common.CustomException;
import com.tiheima.reggie.mapper.CategoryMapper;
import com.tiheima.reggie.pojo.Category;
import com.tiheima.reggie.pojo.Dish;
import com.tiheima.reggie.pojo.Setmeal;
import com.tiheima.reggie.service.CategoryService;
import com.tiheima.reggie.service.DishService;
import com.tiheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果关联抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        //查询是否关联套餐
        if(count > 0){
            //抛出异常
            throw new CustomException("当前分类关联菜品不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            //抛出异常
            throw new CustomException("当前分类关联套餐不能删除");
        }
        super.removeById(id);
        //正常删除
    }
}
