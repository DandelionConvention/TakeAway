package com.tiheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiheima.reggie.dto.SetmealDto;
import com.tiheima.reggie.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
