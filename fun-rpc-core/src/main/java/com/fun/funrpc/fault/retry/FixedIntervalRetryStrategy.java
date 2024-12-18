package com.fun.funrpc.fault.retry;

import com.github.rholder.retry.*;
import com.fun.funrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定重试间隔策略
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 19:27
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {
    /**
     * 执行重试
     *
     * @param callable 待执行的任务
     * @return 任务执行结果
     * @throws Exception 任务执行异常
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        // 使用 RetryerBuilder 构建重试器
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                // 使用 retryIfExceptionOfType 方法指定当出现 Exception 异常时重试
               .retryIfExceptionOfType(Exception.class)
                // 使用 withWaitStrategy 方法指定策略，选择 fixedWait 固定时间间隔策略，间隔时间为 3 秒
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                // 使用 withStopStrategy 方法指定策略，选择 stopAfterAttempt 超过最大重试次数停止策略，最大重试次数为 3
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                // 使用 withRetryListener 监听重试，每次重试时，除了再次执行任务外，还能够打印当前的重试次数
                .withRetryListener(new RetryListener() {
                     @Override
                     public <V> void onRetry(Attempt<V> attempt) {
                         log.info("重试次数：{}",attempt.getAttemptNumber());
                     }
                 })
               .build();
        return retryer.call(callable);
    }
}
