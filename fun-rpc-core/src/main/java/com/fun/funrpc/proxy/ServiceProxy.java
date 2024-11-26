package com.fun.funrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fun.funrpc.RpcApplication;
import com.fun.funrpc.model.RpcRequest;
import com.fun.funrpc.model.RpcResponse;
import com.fun.funrpc.serializer.JdkSerializer;
import com.fun.funrpc.serializer.Serializer;
import com.fun.funrpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理（JDK 动态代理）
 * @author FUN
 * @version 1.0
 * @date 2024/11/20 20:59
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
//        JdkSerializer serializer = new JdkSerializer();
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 调用远程服务
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化请求
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 用远程服务，并反序列化结果
            // todo 需要使用注册中心和服务发现机制解决服务地址
            byte[] result = null;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse resResponse = serializer.deserialize(result, RpcResponse.class);
            return resResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
