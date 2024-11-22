package com.fun.funrpc.config;

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
}
