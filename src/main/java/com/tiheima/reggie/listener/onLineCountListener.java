package com.tiheima.reggie.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@WebListener
public class onLineCountListener implements HttpSessionAttributeListener,HttpSessionListener {
    public static int count = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        count++;
        log.info("当前在线人数："+count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        count--;
        log.info("当前在线人数："+count);
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        HttpSessionAttributeListener.super.attributeAdded(se);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        HttpSessionAttributeListener.super.attributeRemoved(se);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        HttpSessionAttributeListener.super.attributeReplaced(se);
    }
}
