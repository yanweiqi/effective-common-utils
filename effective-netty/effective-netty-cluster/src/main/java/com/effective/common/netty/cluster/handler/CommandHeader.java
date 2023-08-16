package com.effective.common.netty.cluster.handler;

import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.protocol.Header;
import com.effective.common.netty.cluster.transport.serialization.Serialization;
import com.effective.common.netty.cluster.transport.serialization.SerializerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.CodecException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description
 *
 * @date 2020/2/26
 */
@Slf4j
@Data
public class CommandHeader implements Header {

    /**
     * Command body length
     * <p>
     * The total length: length of (header+body), not include MEGICCODE
     * </p>
     */
    protected Integer length;

    /**
     * Header length of (PROTOCOLTYPE+...+header tail), not include FULLLENGTH and HEADERLENGTH
     */
    protected Short headerLength;

    /**
     * Protocol type
     */
    protected byte protocolType = (byte) 1;

    /**
     * Serialization type
     * <p>
     * 1. json
     * 2. protostuff
     * </p>
     */
    protected byte serialization = Serialization.PROTOSTUFF_ID;

    /**
     * Compression type
     */
    protected byte compression = Compression.LZ4;

    /**
     * Command id
     */
    protected int commandId;

    /**
     * Command header version
     */
    private byte version = 1;

    /**
     * 客户端超时时长
     */
    protected int timeout = 5000;

    /**
     * Command name
     */
    private String commandName;

    /**
     * Command origin brokerId
     */
    private String originBrokerId;

    /**
     * Command target brokerId
     */
    private String targetBrokerId;

    /**
     * Extended attributes
     */
    protected volatile Map<Byte, Object> attributes;

    /**
     * 获取或创建扩展属性
     *
     * @return
     */
    protected Map<Byte, Object> getOrCreateAttributes() {
        if (attributes == null) {
            //TODO 是否是单线程操作
            synchronized (this) {
                if (attributes == null) {
                    attributes = new ConcurrentHashMap<>(5);
                }
            }
        }
        return attributes;
    }

    /**
     * 添加扩展属性
     *
     * @param key
     * @param value
     */
    public void addAttribute(final Byte key, final Object value) {
        if (key == null || value == null) {
            return;
        }
        getOrCreateAttributes().put(key, value);
    }

    /**
     * 删除扩展属性
     *
     * @param key
     * @return
     */
    public Object removeAttribute(final Byte key) {
        if (attributes == null || key == null) {
            return null;
        }
        return attributes.remove(key);
    }

    /**
     * 获取扩展属性
     *
     * @param key
     * @return
     */
    public Object getAttribute(final Byte key) {
        if (attributes == null || key == null) {
            return null;
        }
        return attributes.get(key);
    }

    /**
     * 获取扩展属性
     *
     * @param key
     * @return
     */
    public Byte getAttribute(final Byte key, final Byte def) {
        if (attributes == null || key == null) {
            return def;
        }
        Object result = attributes.get(key);
        if (result instanceof Byte) {
            return (Byte) result;
        } else if (result instanceof Number) {
            return ((Number) result).byteValue();
        } else {
            return def;
        }
    }

    /**
     * 获取扩展属性
     *
     * @param key
     * @return
     */
    public Short getAttribute(final Byte key, final Short def) {
        if (attributes == null || key == null) {
            return def;
        }
        Object result = attributes.get(key);
        if (result instanceof Short) {
            return (Short) result;
        } else if (result instanceof Number) {
            return ((Number) result).shortValue();
        } else {
            return def;
        }
    }

    /**
     * 获取扩展属性
     *
     * @param key
     * @return
     */
    public Integer getAttribute(final Byte key, final Integer def) {
        if (attributes == null || key == null) {
            return def;
        }
        Object result = attributes.get(key);
        if (result instanceof Integer) {
            return (Integer) result;
        } else if (result instanceof Number) {
            return ((Number) result).intValue();
        } else {
            return def;
        }
    }

    /**
     * Decode command header
     *
     * @param in
     * @return
     * @throws Exception
     */
    public CommandHeader decode(ByteBuf in) throws Exception {
        if (Objects.nonNull(in)) {
            CommandHeader header = new CommandHeader();
            header.setLength(in.readInt());
            header.setHeaderLength(in.readShort());
            header.setProtocolType(in.readByte());
            header.setSerialization(in.readByte());
            header.setCompression(in.readByte());
            header.setCommandId(in.readInt());
            header.setVersion(in.readByte());
            header.setTimeout(in.readInt());

            header.setCommandName(SerializerUtil.readString(in, false));
            header.setOriginBrokerId(SerializerUtil.readString(in, false));
            header.setTargetBrokerId(SerializerUtil.readString(in, false));
            header.setAttributes(decodeAttributes(in));
            return header;
        }
        return null;
    }

    /**
     * Encode command header
     *
     * @param out
     * @throws Exception
     */
    public int encode(ByteBuf out) throws Exception {
        //写入消息体长度占位
        out.writeInt(0);
        int start = out.writerIndex();
        out.writeShort(0);
        out.writeByte(protocolType);
        out.writeByte(serialization);
        int compressIndex = out.writerIndex();
        out.writeByte(compression);
        out.writeInt(commandId);
        out.writeByte(version);
        out.writeInt(timeout);
        SerializerUtil.write(commandName, out);
        SerializerUtil.write(originBrokerId, out);
        SerializerUtil.write(targetBrokerId, out);
        encodeAttributes(out, attributes);
        int end = out.writerIndex();
        int headLength = end - start;
        setHeaderLength((short) headLength);
        out.writerIndex(start);
        out.writeShort(headLength);
        out.writerIndex(end);
        return compressIndex;
    }

    /**
     * 编码头部扩展信息
     *
     * @param buffer
     * @param attributes
     * @return
     */
    protected void encodeAttributes(final ByteBuf buffer, final Map<Byte, Object> attributes) throws Exception {
        int size = attributes == null ? 0 : attributes.size();
        buffer.writeByte(size);
        if (size > 0) {
            byte key;
            Object val;
            for (Map.Entry<Byte, Object> attr : attributes.entrySet()) {
                key = attr.getKey();
                val = attr.getValue();
                if (val != null) {
                    buffer.writeByte(key);
                    if (val instanceof Integer) {
                        buffer.writeByte((byte) 1);
                        buffer.writeInt((Integer) val);
                    } else if (val instanceof String) {
                        buffer.writeByte((byte) 2);
                        SerializerUtil.write((String) val, buffer);
                    } else if (val instanceof Byte) {
                        buffer.writeByte((byte) 3);
                        buffer.writeByte((Byte) val);
                    } else if (val instanceof Short) {
                        buffer.writeByte((byte) 4);
                        buffer.writeShort((Short) val);
                    } else {
                        throw new CodecException("Value of attrs in message header must be byte/short/int/string");
                    }
                }
            }
        }
    }

    /**
     * 解码扩展属性
     *
     * @param buffer
     * @return
     */
    protected Map<Byte, Object> decodeAttributes(final ByteBuf buffer) throws Exception {
        byte size = buffer.readByte();
        if (size <= 0) {
            return null;
        }
        Map<Byte, Object> attributes = new HashMap<>(size);
        byte key;
        byte type;
        for (int i = 0; i < size; i++) {
            key = buffer.readByte();
            type = buffer.readByte();
            switch (type) {
                case 1:
                    attributes.put(key, buffer.readInt());
                    break;
                case 2:
                    attributes.put(key, SerializerUtil.readString(buffer, false));
                    break;
                case 3:
                    attributes.put(key, buffer.readByte());
                    break;
                case 4:
                    attributes.put(key, buffer.readShort());
                    break;
                default:
                    throw new CodecException("Value of attrs in message header must be byte/short/int/string");

            }
        }
        return attributes;
    }

    @Override
    public CommandHeader clone() throws CloneNotSupportedException {
        CommandHeader header = (CommandHeader) super.clone();
        return header;
    }
}
