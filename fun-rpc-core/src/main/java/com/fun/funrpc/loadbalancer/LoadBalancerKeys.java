package com.fun.funrpc.loadbalancer;

/**
 * 负载均衡器的key常量
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/1 16:16
 */
public interface LoadBalancerKeys {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性hash
     */
    String CONSISTENT_HASH = "consistentHash";
}
