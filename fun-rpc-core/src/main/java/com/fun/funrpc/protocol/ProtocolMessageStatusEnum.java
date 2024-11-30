package com.fun.funrpc.protocol;

import lombok.Getter;

/**
 * 协议消息的状态枚举类，成功、失败（相应 请求）、超时等状态
 * @author FUN
 * @version 1.0
 * @date 2024/11/29 16:27
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok", 20),
    BAD_REQUEST("badRequest", 40),
    BAD_RESPONSE("badResponse", 50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举对象
     *
     * @param value
     * @return
     */
    public static  ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum statusEnum : ProtocolMessageStatusEnum.values()) {
            if (statusEnum.getValue() == value) {
                return statusEnum;
            }
        }
        return null;
    }
}
