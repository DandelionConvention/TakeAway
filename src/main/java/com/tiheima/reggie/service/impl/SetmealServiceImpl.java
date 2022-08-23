package com.tiheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiheima.reggie.common.CustomException;
import com.tiheima.reggie.dto.SetmealDto;
import com.tiheima.reggie.mapper.SetmealMapper;
import com.tiheima.reggie.pojo.Setmeal;
import com.tiheima.reggie.pojo.SetmealDish;
import com.tiheima.reggie.service.SetmealDishService;
import com.tiheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存基本信息
        this.save(setmealDto);
        // 保存套餐和菜品的关联信息

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishList);


    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询状态是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);

        // 不能删除的抛出异常

        if(count > 0){
            throw new CustomException("套餐正在售卖，不可以删除");
        }

        // 删除套餐
        this.removeByIds(ids);

        // 删除关系
        LambdaQueryWrapper<SetmealDish> queryWrapperd = new LambdaQueryWrapper<>();
        queryWrapperd.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(queryWrapperd);
    }
}
