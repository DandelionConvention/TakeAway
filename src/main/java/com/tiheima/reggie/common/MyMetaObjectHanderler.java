package com.tiheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据处理器
 *
 * 只能处理同时拥有这些字段的数据，没有做判断按
 */
@Component
@Slf4j
public class MyMetaObjectHanderler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasSetter("createTime")){
            // 一个个属性判断，增加通用性
            metaObject.setValue("createTime", LocalDateTime.now());
        }

        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime",LocalDateTime.now());
        }

        //如何获取Session，来设置id
        log.info("自动填充字段"+BaseContext.getCurrentId());

        if(metaObject.hasSetter("createUser")){
            metaObject.setValue("createUser",BaseContext.getCurrentId());
        }

        if(metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime",LocalDateTime.now());
        }
        if(metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
        }
    }
}
