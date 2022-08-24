package com.tiheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tiheima.reggie.common.R;
import com.tiheima.reggie.pojo.User;
import com.tiheima.reggie.service.UserService;
import com.tiheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/code")
    public R<String> sendMsg(@RequestParam String phone, HttpSession session){
        // 获取手机号
//        String phone = user.getPhone();
        //生成验证码
        if(StringUtils.isNotEmpty(phone)){
            Integer code = ValidateCodeUtils.generateValidateCode(4);

            log.info("验证码生成：{}",code);

            // 保存session
            session.setAttribute(phone,code.toString());

            return R.success("发送成功");
        }
        // 发送短信

        return R.error("发送失败");


    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){
        // 获取手机号
        String phone = (String) user.get("phone");
        Object code = session.getAttribute(phone);

        if(!user.get("code").equals(code)){
            log.info(user.get("code").toString()+"---"+code.toString());
            return R.error("验证码错误");
        }

        // 判断数据库中是否又用户，没有就直接注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);

        User userD = userService.getOne(queryWrapper);
        if(userD == null){
            // 注册新用户
            userD = new User();
            userD.setPhone(phone);
            userD.setStatus(1);
            userService.save(userD);
        }
        session.setAttribute("user",userD.getId());
        return R.success(userD);

    }
}
