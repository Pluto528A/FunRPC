package com.fun.funrpc.registry;

import com.fun.funrpc.spi.SpiLoader;

/**
 * 注册中心工厂类（获取注册中心对象）
 * @author FUN
 * @version 1.0
 * @date 2024/11/28 17:16
 */
public class RegistryFactory {

    /**
     * Spi机制获取注册中心对象
     */
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心对象（etcd）
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取注册中心对象实例
     * @return 注册中心对象
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
