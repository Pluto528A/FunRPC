package com.fun.funrpc.proxy;

import com.fun.funrpc.RpcApplication;

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
        // 判断是否开启Mock模式
        System.out.println(RpcApplication.getRpcConfig());
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass},  new ServiceProxy());
    }

    /**
     * 根据服务类获取Mock代理对象
     * @param serviceClass 接口类
     * @param <T> 接口类型
     * @return
     */
    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
