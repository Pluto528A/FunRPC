package com.fun.funrpc.proxy;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂 （创建代理对象）
 * @author FUN
 * @version 1.0
 * @date 2024/11/20 21:05
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     *
     * @param serviceClass 接口类
     * @param <T> 接口类型
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
