package com.fun.funrpc.server.tcp;

import com.fun.funrpc.protocol.ProtocolConstant;
import com.fun.funrpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

/**
 * TCP 服务端实现
 *
 * @author FUN
 * @version 1.0
 * @date 2024/11/29 16:49
 */
@Slf4j
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // 这里编写处理请求的逻辑，根据请求数据，返回响应数据
        return "Hello, Client!".getBytes();
    }

    /**
     * 启动 Http 服务
     *
     * @param port 端口号
     */
    @Override
    public void doStart(int port) {
        // 创建 Vertx 对象
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务端
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(new TcpServerHandler());

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP Server started on port " + port);
            } else {
                System.out.println("TCP Server start failed on port " + port);
            }
        });
    }

    public static void main(String[] args) {
        VertxTcpServer server = new VertxTcpServer();
        server.doStart(8888);
    }
}
