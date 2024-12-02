package com.fun.funrpc.fault.retry;

import com.fun.funrpc.spi.SpiLoader;

/**
 * 工厂模式，支持根据 key 从 SPI 加载对应的重试策略
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 19:44
 */
public class RetryStrategyFactory {

    static {
        // 加载 SPI 实现类
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试机制：不重试
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 根据 key 从 SPI 加载对应的重试策略
     *
     * @param key 策略 key
     * @return 对应的重试策略
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
