package com.fun.funrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 22:22
 */
public class LocalRegistry {

    /**
     * 注册信息存储
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 获取服务类
     * @param serviceName 服务名称
     * @return 服务类
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param serviceClass 服务类
     */
    public static void register(String serviceName, Class<?> serviceClass) {
        map.put(serviceName, serviceClass);
    }

    /**
     * 注销服务
     * @param serviceName 服务名称
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
