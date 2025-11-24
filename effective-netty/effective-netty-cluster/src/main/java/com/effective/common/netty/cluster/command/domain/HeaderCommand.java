package com.effective.common.netty.cluster.command.domain;

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

@Slf4j
@Data
public class HeaderCommand implements Header {

    /**
     * Command body length
     * The total length: length of (header+body), not include MEGICCODE
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
     * 1. json
     * 2. protostuff
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
            //是否是单线程操作
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
     * @param in 字节流
     * @return 命令头部解码
     * @throws Exception 命令异常
     */
    public HeaderCommand decode(ByteBuf in) throws Exception {
        if (Objects.nonNull(in)) {
            HeaderCommand header = new HeaderCommand();
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
     * @param out 字节流编码
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
    protected void encodeAttributes(final ByteBuf out, final Map<Byte, Object> attributes) throws Exception {
        if (attributes == null || attributes.isEmpty()) {
            return; // Skip processing if attributes are null or empty
        }
        for (Map.Entry<Byte, Object> attr : attributes.entrySet()) {
            byte key = attr.getKey();
            Object val = attr.getValue();
            if (val != null) {
                out.writeByte(key);
                if (val instanceof Integer) {
                    out.writeByte((byte) 1);
                    out.writeInt((Integer) val);
                } else if (val instanceof String) {
                    out.writeByte((byte) 2);
                    SerializerUtil.write((String) val, out);
                } else if (val instanceof Byte) {
                    out.writeByte((byte) 3);
                    out.writeByte((Byte) val);
                } else if (val instanceof Short) {
                    out.writeByte((byte) 4);
                    out.writeShort((Short) val);
                } else {
                    throw new CodecException("Value of attrs in message header must be byte/short/int/string");
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
    public HeaderCommand clone() throws CloneNotSupportedException {
        HeaderCommand header = (HeaderCommand) super.clone();
        return header;
    }

    public void setAttributes(Map<Byte, Object> attributes) {
        this.attributes = attributes;
    }
}
