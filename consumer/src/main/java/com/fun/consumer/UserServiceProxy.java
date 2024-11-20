package com.fun.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fun.common.model.User;
import com.fun.common.service.UserService;
import com.fun.funrpc.model.RpcRequest;
import com.fun.funrpc.model.RpcResponse;
import com.fun.funrpc.serializer.JdkSerializer;
import com.fun.funrpc.serializer.Serializer;

/**
 * @author FUN
 * @version 1.0
 * @date 2024/11/20 20:45
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 调用远程服务
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 用远程服务，并反序列化结果
            byte[] result = null;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                   .header("Content-Type", "application/octet-stream")
                   .body(bodyBytes)
                   .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse resResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) resResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
