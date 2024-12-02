package com.fun.funrpc.fault.tolerant;

import com.fun.funrpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败策略（立刻通知外层调用方，失败则直接抛出异常）
 * 就是遇到异常后，将异常再次抛出，交给外层处理
 *
 * @author FUN
 * @version 1.0
 * @date 2024/12/2 21:13
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    /**
     * 容错处理
     *
     * @param context 请求上下文 用于传递数据
     * @param e       异常
     * @return 容错后的响应
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错，请检查服务是否正常", e);
    }
}
