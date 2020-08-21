package com.xliu.qch.baseadmin.config.websocket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;

/**
 * In order to solve the problem that WebSocket can't inject to other Class
 * Reference:https://blog.csdn.net/tornadojava/article/details/78781474
 */
public class MyEndpointConfigure extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz){
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MyEndpointConfigure.context = applicationContext;
    }
}
