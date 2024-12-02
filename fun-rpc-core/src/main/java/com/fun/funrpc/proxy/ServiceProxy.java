package com.fun.funrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fun.funrpc.RpcApplication;
import com.fun.funrpc.config.RpcConfig;
import com.fun.funrpc.constant.RpcConstant;
import com.fun.funrpc.fault.retry.RetryStrategy;
import com.fun.funrpc.fault.retry.RetryStrategyFactory;
import com.fun.funrpc.loadbalancer.LoadBalancer;
import com.fun.funrpc.loadbalancer.LoadBalancerFactory;
import com.fun.funrpc.model.RpcRequest;
import com.fun.funrpc.model.RpcResponse;
import com.fun.funrpc.model.ServiceMetaInfo;
import com.fun.funrpc.protocol.*;
import com.fun.funrpc.registry.Registry;
import com.fun.funrpc.registry.RegistryFactory;
import com.fun.funrpc.serializer.JdkSerializer;
import com.fun.funrpc.serializer.Serializer;
import com.fun.funrpc.serializer.SerializerFactory;
import com.fun.funrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 调用远程服务
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化请求
        try {
            // 用远程服务，并反序列化结果
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 使用注册中心和服务发现机制解决服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstace(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new Exception("服务地址未找到");
            }

            // 使用负载均衡算法，选择一个可用的服务地址
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());

            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> resquestParams = new HashMap<>();
            resquestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(resquestParams, serviceMetaInfoList);

            /**
             * TcpClient 方式调用远程服务
             */
            // 使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            // 发送 TCP 请求
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo));
            return rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
