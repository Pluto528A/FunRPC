package com.fun.funrpc;

import com.fun.funrpc.config.RegistryConfig;
import com.fun.funrpc.config.RpcConfig;
import com.fun.funrpc.constant.RpcConstant;
import com.fun.funrpc.registry.Registry;
import com.fun.funrpc.registry.RegistryFactory;
import com.fun.funrpc.serializer.Serializer;
import com.fun.funrpc.serializer.SerializerFactory;
import com.fun.funrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Rpc 框架启动类
 * 存放了项目全局用到的变量，双检锁单例模式
 * @author FUN
 * @version 1.0
 * @date 2024/11/22 21:03
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，传入自定义配置信息
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("RpcApplication init success, rpcConfig: {}", newRpcConfig.toString());

        // 初始化注册中心
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstace(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("Registry init success, registryConfig: {}", registryConfig.toString());

        // 创建并注册 ShutdownHook，JVM 停止时销毁注册中心
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    public static void  init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
            // 配置加载失败，使用默认配置
            log.error("RpcApplication init error, use default config, error: {}", e.getMessage());
            return;
        }
        init(newRpcConfig);
    }

    /**
     * 获取 Rpc 配置信息
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    rpcConfig = new RpcConfig();
                }
            }
        }
        return rpcConfig;
    }

}
