package com.tiheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiheima.reggie.common.R;
import com.tiheima.reggie.pojo.Employee;
import com.tiheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //request来存放session

        // 密码md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp == null){
            return R.error("登陆失败");
        }

        // 密码比对
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }else {
            emp.setPassword(null);
        }

        //查看员工状态
        if(emp.getStatus() == 0){
            return R.error("账号禁用");
        }

        //登陆成功
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        request.getSession().invalidate();

        return R.success("退出成功");
    }

    @GetMapping("/test")
    public R<Object> testLogin(HttpServletRequest request){
        Object emp =  request.getSession().getAttribute("employee");
        return R.success(emp);
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("add employee:"+employee.toString());
        //需要初始密码
        String pwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(pwd);

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        // 验证是否重名，使用全局异常处理
        employeeService.save(employee);

        return R.success("新增用户成功");
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("修改："+employee.toString());
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }

        return R.error("没有查询到");
    }

}
