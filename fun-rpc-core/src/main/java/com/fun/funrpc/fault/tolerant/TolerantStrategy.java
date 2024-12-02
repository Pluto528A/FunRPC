package com.fun.funrpc.fault.tolerant;

import com.fun.funrpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错机制
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 21:11
 */
public interface TolerantStrategy {

    /**
     * 容错处理
     *
     * @param context 请求上下文 用于传递数据
     * @param e       异常
     * @return 容错后的响应
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
