package com.fun.funrpc.fault.tolerant;

import com.fun.funrpc.spi.SpiLoader;

/**
 * 容错策略工厂
 * 工厂模式，支持根据 key 从 SPI 获取容错策略对象实例
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 21:18
 */
public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     */
    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
