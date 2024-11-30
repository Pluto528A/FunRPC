package com.fun.funrpc.protocol;

import com.fun.funrpc.serializer.Serializer;
import com.fun.funrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

/**
 * 消息编码器
 *
 * @author FUN
 * @version 1.0
 * @date 2024/11/30 16:08
 */
public class ProtocolMessageEncoder {
    /**
     * 编码消息
     * 依次向缓冲区中写入消息对象的字段，序列化消息体，并返回字节缓冲区
     *
     * @param protocolMessage 消息对象
     * @return 字节缓冲区
     * @throws Exception 序列化异常
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws Exception {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();

        // 依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化协议不存在");
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());

        // 写入 body 长度和数据
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);

        return buffer;
    }
}
