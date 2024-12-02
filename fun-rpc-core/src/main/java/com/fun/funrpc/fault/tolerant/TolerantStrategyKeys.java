package com.fun.funrpc.fault.tolerant;

/**
 * 容错策略的key
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 21:17
 */
public interface TolerantStrategyKeys {

    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";
}
