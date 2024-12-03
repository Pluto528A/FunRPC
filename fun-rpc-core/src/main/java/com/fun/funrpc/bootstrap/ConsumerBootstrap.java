package com.fun.funrpc.bootstrap;

import com.fun.funrpc.RpcApplication;

/**
 * 消费者启动类
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/3 15:13
 */
public class ConsumerBootstrap {

    /**
     * 初始化 RPC 框架
     */
    public static void init() {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
    }

}
