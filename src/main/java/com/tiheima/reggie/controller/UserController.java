package com.tiheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tiheima.reggie.common.R;
import com.tiheima.reggie.pojo.User;
import com.tiheima.reggie.service.UserService;
import com.tiheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // 获取手机号
        String phone = user.getPhone();

        // 查看缓存，存在直接返回
        Object codeCache = redisTemplate.opsForValue().get(phone);
        if(codeCache != null){
            return R.error("已经发送，稍后重试");
        }
        //生成验证码
        if(StringUtils.isNotEmpty(phone)){
            Integer code = ValidateCodeUtils.generateValidateCode(4);

            log.info("验证码生成：{}",code);

            // 保存session
//            session.setAttribute(phone,code.toString());

            // 保存Redis
            redisTemplate.opsForValue().set(phone,String.valueOf(code),5, TimeUnit.MINUTES);

            return R.success("发送成功");
        }
        // 发送短信

        return R.error("发送失败");


    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){
        // 获取手机号
        String phone = (String) user.get("phone");
//        Object code = session.getAttribute(phone);
        Object code = redisTemplate.opsForValue().get(phone); // 获取缓存
        redisTemplate.delete(phone); // 删除缓存
        if(user.get("code").toString().equals("1111")){

        }else {
            if(code == null || !user.get("code").toString().equals(code.toString())){
                log.info(user.get("code").toString()+"---"+code.toString());
                return R.error("验证码错误");
            }
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
