package com.fun.funrpc.fault.retry;

/**
 * 重试策略常量
 * 列举所有支持的重试策略键名
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 19:42
 */
public interface RetryStrategyKeys {

    /**
     * 无重试策略
     */
    String NO = "no";

    /**
     * 固定间隔重试策略
     */
    String FIXED_INTERVAL = "fixedInterval";
}
