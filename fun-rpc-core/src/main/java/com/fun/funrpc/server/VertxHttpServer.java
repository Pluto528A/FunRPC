package com.fun.funrpc.server;

import io.vertx.core.Vertx;

/**
 * Vet.x HTTP 服务器
 * @author FUN
 * @version 1.0
 * @date 2024/11/19 22:13
 */
public class VertxHttpServer implements HttpServer {

    /**
     * 启动 HTTP 服务器
     * @param port 监听端口
     */
    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        // 每当有客户端向这个 HTTP 服务器发送 HTTP 请求时，服务器会自动调用这个 HttpServerHandler 实例中相应的方法来处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动 HTTP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP server started on port " + port);
            } else {
                System.out.println("HTTP server start failed: " + result.cause());
            }
        });
    }
}
