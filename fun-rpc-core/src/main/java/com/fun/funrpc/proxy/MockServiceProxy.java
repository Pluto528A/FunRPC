package com.fun.funrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理
 * @author FUN
 * @version 1.0
 * @date 2024/11/25 19:19
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {


    /**
     * 调用代理
     * @param proxy
     * @param method
     * @param args
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("Mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 生成指定类型的默认值对象
     * @param methodReturnType
     * @return
     */
    private Object getDefaultObject(Class<?> methodReturnType) {
        // 基本类型
        if (methodReturnType.isPrimitive()) {
            if (methodReturnType == int.class) {
                return 0;
            } else if (methodReturnType == long.class) {
                return 0L;
            } else if (methodReturnType == double.class) {
                return 0.0;
            } else if (methodReturnType == float.class) {
                return 0.0f;
            } else if (methodReturnType == boolean.class) {
                return false;
            } else if (methodReturnType == byte.class) {
                return (byte) 0;
            } else if (methodReturnType == short.class) {
                return (short) 0;
            } else if (methodReturnType == char.class) {
                return '\u0000';
            }
        }

        return null;
    }
}
