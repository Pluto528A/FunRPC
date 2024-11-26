package com.fun.provider;

import com.fun.common.service.UserService;
import com.fun.funrpc.RpcApplication;
import com.fun.funrpc.registry.LocalRegistry;
import com.fun.funrpc.server.HttpServer;
import com.fun.funrpc.server.VertxHttpServer;

/**
 * 简单 Rpc 服务提供者示例
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 21:57
 */
public class EasyProvider {
    public static void main(String[] args) {

        RpcApplication.init();
        // 注册 rpc 服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 rpc 服务提供者
        HttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
