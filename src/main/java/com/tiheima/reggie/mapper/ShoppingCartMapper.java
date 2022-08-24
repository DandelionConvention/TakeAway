package com.tiheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiheima.reggie.pojo.ShoppingCart;
import com.tiheima.reggie.service.ShoppingCartService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
