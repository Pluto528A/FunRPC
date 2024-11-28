package com.fun.funrpc.config;

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
}
