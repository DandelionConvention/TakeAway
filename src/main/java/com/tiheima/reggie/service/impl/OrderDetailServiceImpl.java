package com.tiheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiheima.reggie.mapper.OrderDetailMapper;
import com.tiheima.reggie.pojo.OrderDetail;
import com.tiheima.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {
}
