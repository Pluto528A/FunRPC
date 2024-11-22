package com.fun.funrpc.server;

/**
 * Http 服务接口
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 22:11
 */
public interface HttpServer {

    /**
     * 启动 Http 服务
     * @param port 端口号
     */
    void doStart(int port);
}
