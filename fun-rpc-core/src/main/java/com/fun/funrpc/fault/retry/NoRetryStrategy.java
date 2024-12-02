package com.fun.funrpc.fault.retry;

import com.fun.funrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 19:26
 */
@Slf4j
public class NoRetryStrategy implements RetryStrategy {
    /**
     * 执行重试
     *
     * @param callable 待执行的任务
     * @return 任务执行结果
     * @throws Exception 任务执行异常
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
