package com.fun.funrpc.bootstrap;

import com.fun.funrpc.RpcApplication;
import com.fun.funrpc.config.RegistryConfig;
import com.fun.funrpc.config.RpcConfig;
import com.fun.funrpc.model.ServiceMetaInfo;
import com.fun.funrpc.model.ServiceRegisterInfo;
import com.fun.funrpc.registry.LocalRegistry;
import com.fun.funrpc.registry.Registry;
import com.fun.funrpc.registry.RegistryFactory;
import com.fun.funrpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者启动类
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/3 14:43
 */
public class ProviderBootstrap {

    /**
     * 初始化 RPC 框架
     * @param serviceRegisterInfoList
     */
    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList) {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        // 启动 HTTP 服务
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

        // 启动 TCP 服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
