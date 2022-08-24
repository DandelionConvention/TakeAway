package com.tiheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiheima.reggie.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
