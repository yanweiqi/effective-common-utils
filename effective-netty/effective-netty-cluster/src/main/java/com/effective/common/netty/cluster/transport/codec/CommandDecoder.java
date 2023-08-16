package com.effective.common.netty.cluster.transport.codec;


import com.effective.common.netty.cluster.command.BaseCommand;
import com.effective.common.netty.cluster.command.CommandClassType;
import com.effective.common.netty.cluster.handler.CommandHeader;
import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.compression.CompressionSelector;
import com.effective.common.netty.cluster.transport.protocol.CommandProtocol;
import com.effective.common.netty.cluster.transport.protocol.Protocol;
import com.effective.common.netty.cluster.transport.serialization.Serialization;
import com.effective.common.netty.cluster.transport.serialization.SerializationSelector;
import com.effective.common.netty.cluster.utils.JSONUtil;
import com.effective.common.netty.cluster.utils.io.ChannelUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Objects;

/**
 * Command decoder
 *
 *
 */
@Slf4j
public class CommandDecoder extends LengthFieldBasedFrameDecoder {

    private final Protocol protocol;

    private static final int FRAME_MAX_LENGTH = 16777216;

    public CommandDecoder() {
        super(FRAME_MAX_LENGTH, 0, 4, -4, 0);
        protocol = new CommandProtocol();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            in.markReaderIndex();
            if (in.readableBytes() < protocol.getMagicCode().length) {
                log.error("[###CommandDecoder###]Readable bytes is less than magic code length!");
                return null;
            }
            byte[] magicCode = new byte[protocol.getMagicCode().length];
            in.getBytes(in.readerIndex(), magicCode);
            if (!Arrays.equals(magicCode, protocol.getMagicCode())) {
                log.error("[###CommandDecoder###]This type of magicCode is not supported!");
                return null;
            } else {
                in.skipBytes(magicCode.length);
            }
            frame = (ByteBuf) super.decode(ctx, in);
            if (Objects.isNull(frame)) {
                if (log.isInfoEnabled()) {
                    log.info("[###CommandDecoder###]Decode error, frame is null!");
                }
                in.resetReaderIndex();
                return null;
            }
            CommandHeader header = new CommandHeader().decode(frame);
            if (Objects.isNull(header)) {
                log.error("[###CommandDecoder###]Decode error, header is null!");
                in.resetReaderIndex();
                return null;
            }
            Serialization serialization = SerializationSelector.select(header.getSerialization());
            if (serialization == null) {
                throw new CodecException(String.format("Error occurs while decoding. unknown serialization type %d!", header.getSerialization()));
            }
            Compression compression = CompressionSelector.select(header.getCompression());
            InputStream inputStream = new ByteBufInputStream(frame);
            inputStream = compression == null ? inputStream : compression.decompress(inputStream);
            if (log.isDebugEnabled()) {
                log.debug("[###CommandDecoder###]Decode compression={}, inputStream={}", header.getCompression(), inputStream.getClass().getCanonicalName());
            }
            CommandClassType commandClassType = CommandClassType.nameOf(header.getCommandName());
            // Object payload = serialization.getSerializer().deserialize(inputStream,
            //         ((ParameterizedType) commandClassType.getClazz().getGenericSuperclass()).getActualTypeArguments()[0]);
            BaseCommand command = (BaseCommand) commandClassType.getClazz().newInstance();
            command.setHeader(header);
            command.setCommandBody(deserialize(serialization, inputStream, commandClassType));
            if (log.isDebugEnabled()) {
                log.debug("[###CommandDecoder###]Decode success, command={}, type={}", JSONUtil.bean2Json(command),
                        command.getClass().getCanonicalName());
            }
            return command;
        } catch (Exception ex) {
            log.error("[###CommandDecoder###]Decode exception, message={}", ex.getMessage(), ex);
            ChannelUtil.closeChannel(ctx.channel());
        } finally {
            if (null != frame) {
                frame.release();
            }
        }
        return null;
    }

    /**
     * Deserialize
     *
     * @param serialization
     * @param inputStream
     * @param commandClassType
     * @return
     */
    private Object deserialize(Serialization serialization, InputStream inputStream, CommandClassType commandClassType) throws IOException {
        return serialization.getSerializer().deserialize(inputStream, getPayloadClass(commandClassType));
    }

    /**
     * 获取命令的消息体类型
     *
     * @param type 命令类型
     * @return 包体类
     */
    protected Class getPayloadClass(final CommandClassType type) {
        return (Class) ((ParameterizedType) type.getClazz().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
