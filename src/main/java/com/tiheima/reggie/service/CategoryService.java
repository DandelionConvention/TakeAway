package com.tiheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiheima.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
