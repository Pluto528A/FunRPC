package com.fun.funrpc.fault.tolerant;

import com.fun.funrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理容错策略实现
 * 就是遇到异常后，记录一条日志，然后正常返回一个响应对象，就好像没有出现过报错
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 21:15
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {
    /**
     * 容错处理
     *
     * @param context 请求上下文 用于传递数据
     * @param e       异常
     * @return 容错后的响应
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常", e);
        return new RpcResponse();
    }
}
