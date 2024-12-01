package com.fun.funrpc.loadbalancer;

import com.fun.funrpc.model.ServiceMetaInfo;

import java.security.Provider;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡器接口（消费端使用）
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/1 15:59
 */
public interface LoadBalancer {
    /**
     * 选择服务提供者
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 服务元信息列表
     * @return 服务提供者
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
