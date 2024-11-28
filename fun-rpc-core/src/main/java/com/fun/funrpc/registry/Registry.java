package com.fun.funrpc.registry;

import com.fun.funrpc.config.RegistryConfig;
import com.fun.funrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口
 * 后续可以实现多种不同的注册中心，如：zookeeper、redis、consul等
 * @author FUN
 * @version 1.0
 * @date 2024/11/27 21:06
 */
public interface Registry {

    /**
     * 初始化注册中心
     * @param registryConfig 注册中心配置
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务 (服务端)
     * @param serviceMetaInfo 服务元信息
     * @throws Exception 注册异常
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;



    /**
     * 注销服务 (服务端)
     * @param serviceMetaInfo 服务元信息
     * @throws Exception 注销异常
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现 (获取某服务的所有节点信息，客户端)
     * @param serviceKey 服务key
     * @return 服务元信息列表
     * @throws Exception 服务发现异常
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws Exception;

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     * @throws Exception 心跳异常
     */
    void heartBeat();

    /**
     * 监听服务节点变化
     * @param serviceNodeKey 服务节点key
     */
    void watch(String serviceNodeKey);
}
