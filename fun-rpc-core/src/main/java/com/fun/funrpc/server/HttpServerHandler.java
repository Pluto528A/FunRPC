package com.fun.funrpc.server;

import com.fun.funrpc.RpcApplication;
import com.fun.funrpc.model.RpcRequest;
import com.fun.funrpc.model.RpcResponse;
import com.fun.funrpc.registry.LocalRegistry;
import com.fun.funrpc.serializer.JdkSerializer;
import com.fun.funrpc.serializer.Serializer;
import com.fun.funrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * HTTP请求处理器
 * @author FUN
 * @version 1.0
 * @date 2024/11/20 20:05
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 记录日志
        System.out.println("receive request: " + request.method() + " " + request.uri());

        // 异步处理 HTTP 请求
        request.bodyHandler(body -> {
           byte[] bytes = body.getBytes();

           // 反序列化请求对象
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 构造相应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 请求为空直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("请求为空");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // 根据服务名和方法名 调用服务, 反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                // 封装结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("OK");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 响应请求
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应请求
     * @param request 请求对象
     * @param rpcResponse 响应对象
     * @param serializer 序列化器
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("Content-Type", "application/octet-stream");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
