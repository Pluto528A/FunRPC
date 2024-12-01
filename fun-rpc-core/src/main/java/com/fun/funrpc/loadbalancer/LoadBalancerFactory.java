package com.fun.funrpc.loadbalancer;

import com.fun.funrpc.spi.SpiLoader;

/**
 * 负载均衡器工厂类
 * 工厂模式，支持根据 key 从 SPI 加载对应的 LoadBalancer 实现
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/1 16:18
 */
public class LoadBalancerFactory {

    static {
        // 加载 SPI 实现
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认的负载均衡器实现（轮询）
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 根据 key 从 SPI 加载对应的 LoadBalancer 实现
     * 如果没有配置 key，则使用默认的轮询负载均衡器
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }

}
