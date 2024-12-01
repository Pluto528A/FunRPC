package com.fun.funrpc.loadbalancer;

import com.fun.funrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性Hash负载均衡算法
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/1 16:07
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性 Hash 环， 存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    private static final int VIRTUAL_NODE_NUM = 100;

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

        // 构建一致性 Hash 环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // 获取调用请求的 Hash 值
        int hash = getHash(requestParams);

        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
            entry = virtualNodes.firstEntry();
        }

        return entry.getValue();
    }

    /**
     * 获取 Hash 值（可选择别的 Hash 函数）
     *
     * @param key 键
     * @return Hash 值
     */
    private int getHash(Object key) {
        return key.hashCode();
    }
}
