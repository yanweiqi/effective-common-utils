package com.effective.common.netty.cluster.transport.serialization;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Serializer
 *
 *
 * @date 2020/2/27
 */
@Slf4j
public class SerializerUtil {

    protected static byte[] getBytes(String value, Charset charset) {
        if (value == null) {
            return new byte[0];
        } else {
            byte[] bytes;
            if (charset == null) {
                bytes = value.getBytes(StandardCharsets.UTF_8);
            } else {
                bytes = value.getBytes(charset);
            }
            return bytes;
        }
    }

    public static String readString(ByteBuf in, boolean compressed) throws Exception {
        return read(in, in.readInt(), compressed, StandardCharsets.UTF_8);
    }

    public static String read(ByteBuf in, int length, boolean compressed, Charset charset) throws Exception {
        if (length <= 0) {
            return null;
        } else {
            byte[] bytes = readBytes(in, length);
            try {
                if (compressed) {
                    // TODO 压缩算法
                    // bytes = ZipUtil.decompressByZlib(bytes, 0, bytes.length);
                }
                if (charset == null) {
                    charset = StandardCharsets.UTF_8;
                }
                return new String(bytes, charset);
            } catch (Exception e) {
                return new String(bytes);
            }
        }
    }

    public static byte[] readBytes(ByteBuf in, int length) throws Exception {
        if (in != null && length > 0) {
            int len = in.readableBytes();
            if (len == 0) {
                return new byte[0];
            } else {
                if (length < len) {
                    len = length;
                }

                byte[] bytes = new byte[len];
                in.readBytes(bytes);
                return bytes;
            }
        } else {
            return new byte[0];
        }
    }

    public static void write(int value, ByteBuf out, int lengthSize) throws Exception {
        if (out != null) {
            switch(lengthSize) {
                case 1:
                    out.writeByte(value);
                    break;
                case 2:
                    out.writeShort(value);
                case 3:
                default:
                    break;
                case 4:
                    out.writeInt(value);
            }

        }
    }

    public static void write(long value, ByteBuf out) throws Exception {
        if (out != null) {
            out.writeLong(value);
        }
    }

    public static void write(String value, ByteBuf out) throws Exception {
        write(value, out, 4, false);
    }

    public static void write(String value, ByteBuf out, int lengthSize) throws Exception {
        write(value, out, lengthSize, false);
    }

    public static void write(String value, ByteBuf out, int lengthSize, boolean compressed) throws Exception {
        if (out != null) {
            if (value != null && !value.isEmpty()) {
                byte[] bytes = getBytes(value, StandardCharsets.UTF_8);
                if (compressed) {
                    // TODO 压缩算法
                    // bytes = ZipUtil.compressByZlib(bytes, 0, bytes.length);
                }
                write(bytes.length, out, lengthSize);
                out.writeBytes(bytes);
            } else {
                write(0, out, lengthSize);
            }

        }
    }

    public static void write(String value, ByteBuf out, Charset charset) throws Exception {
        if (out != null && value != null) {
            out.writeBytes(getBytes(value, charset));
        }
    }

    public static void write(ByteBuffer value, ByteBuf out) throws Exception {
        write(value, out, true);
    }

    public static void write(ByteBuffer value, ByteBuf out, boolean writeLength) throws Exception {
        int length = value == null ? 0 : value.remaining();
        if (writeLength) {
            out.writeInt(length);
        }

        if (length > 0) {
            if (value.hasArray()) {
                out.writeBytes(value.array(), value.arrayOffset() + value.position(), value.remaining());
            } else {
                out.writeBytes(value.slice());
            }
        }

    }
}
