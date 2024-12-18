package com.fun.funrpc.config;

import com.fun.funrpc.fault.retry.RetryStrategyKeys;
import com.fun.funrpc.fault.tolerant.TolerantStrategyKeys;
import com.fun.funrpc.loadbalancer.LoadBalancerKeys;
import com.fun.funrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC 配置类
 * @author FUN
 * @version 1.0
 * @date 2024/11/22 20:37
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "fun-rpc";

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 服务端主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务端端口
     */
    private Integer serverPort = 8080;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     * NO：不重试
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
