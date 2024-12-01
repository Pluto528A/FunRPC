package com.fun.funrpc.loadbalancer;

import com.fun.funrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/1 16:05
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

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

        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }

        // 只有一个服务提供者，直接返回
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        // 随机选择一个服务提供者
        int index = random.nextInt(size);
        return serviceMetaInfoList.get(index);
    }
}
