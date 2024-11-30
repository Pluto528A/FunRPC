package com.fun.funrpc.protocol;

import lombok.Getter;

/**
 * 协议消息的类型枚举
 *
 * @author FUN
 * @version 1.0
 * @date 2024/11/29 16:32
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举值
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum protocolMessageTypeEnum : ProtocolMessageTypeEnum.values()) {
            if (protocolMessageTypeEnum.getKey() == key) {
                return protocolMessageTypeEnum;
            }
        }
        return null;
    }
}
