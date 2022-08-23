package com.tiheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiheima.reggie.dto.DishDto;
import com.tiheima.reggie.pojo.Category;
import com.tiheima.reggie.pojo.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品，同时插入菜品对应的口味数据，需要操作dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // 多表查询
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
