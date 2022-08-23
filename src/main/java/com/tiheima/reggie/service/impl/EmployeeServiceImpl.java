package com.tiheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiheima.reggie.mapper.EmpliyeeMapper;
import com.tiheima.reggie.pojo.Employee;
import com.tiheima.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmpliyeeMapper, Employee> implements EmployeeService {
}
