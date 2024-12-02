package com.fun.funrpc.fault.retry;

import com.fun.funrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略接口
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 19:23
 */
public interface RetryStrategy {

    /**
     * 执行重试
     *
     * @param callable 待执行的任务
     * @return 任务执行结果
     * @throws Exception 任务执行异常
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
