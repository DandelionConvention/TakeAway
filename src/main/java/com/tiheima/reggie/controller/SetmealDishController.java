package com.tiheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiheima.reggie.common.R;
import com.tiheima.reggie.dto.SetmealDto;
import com.tiheima.reggie.pojo.Category;
import com.tiheima.reggie.pojo.Setmeal;
import com.tiheima.reggie.service.CategoryService;
import com.tiheima.reggie.service.SetmealDishService;
import com.tiheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealDishController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPageInfo = new Page<>();

        BeanUtils.copyProperties(pageInfo,dtoPageInfo,"records");

        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> recordsDto = records.stream().map((temp) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(temp,setmealDto);

            Category category = categoryService.getById(temp.getCategoryId());

            setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPageInfo.setRecords(recordsDto);

        return R.success(dtoPageInfo);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("成功");
    }

}
