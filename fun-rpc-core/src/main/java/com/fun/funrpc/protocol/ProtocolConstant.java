package com.fun.funrpc.protocol;

/**
 * 协议常量
 * @author FUN
 * @version 1.0
 * @date 2024/11/29 16:24
 */
public interface ProtocolConstant {

    /**
     * 消息头长度（单位：字节）
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数（安全校验，类似安全证书）
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x1;

}
