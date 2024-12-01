package com.fun.funrpc.loadbalancer;

import com.fun.funrpc.model.ServiceMetaInfo;

import java.security.Provider;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/1 16:01
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 当前服务提供者的索引
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    /**
     * 选择服务提供者
     *
     * @param requestParams       请求参数
     * @param serviceMetaInfoList 服务元信息列表
     * @return 服务提供者
     */
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }

        // 只有一个服务提供者，直接返回
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        // 取模算法轮询
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
