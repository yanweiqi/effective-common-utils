package com.effective.common.netty.cluster.transport.codec;


import com.effective.common.netty.cluster.command.api.AbstractCommand;
import com.effective.common.netty.cluster.command.domain.HeaderCommand;
import com.effective.common.netty.cluster.command.factory.CommandFactory;
import com.effective.common.netty.cluster.constants.NettyAttrManager;
import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.compression.CompressionSelector;
import com.effective.common.netty.cluster.transport.protocol.CommandProtocol;
import com.effective.common.netty.cluster.transport.protocol.Protocol;
import com.effective.common.netty.cluster.transport.serialization.Serialization;
import com.effective.common.netty.cluster.transport.serialization.SerializationSelector;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Objects;

/**
 * 解码器
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
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        ByteBuf frame = null;
        try {
            in.markReaderIndex();
            if (in.readableBytes() < protocol.getMagicCode().length) {
                log.error("【解码器】Readable bytes is less than magic code length!");
                return null;
            }
            byte[] magicCode = new byte[protocol.getMagicCode().length];
            in.getBytes(in.readerIndex(), magicCode);
            if (!Arrays.equals(magicCode, protocol.getMagicCode())) {
                log.error("【解码器】This type of magicCode is not supported!");
                return null;
            } else {
                in.skipBytes(magicCode.length);
            }
            frame = (ByteBuf) super.decode(ctx, in);
            if (Objects.isNull(frame)) {
                log.info("【解码器】Decode error, frame is null!");
                in.resetReaderIndex();
                return null;
            }
            HeaderCommand header = new HeaderCommand().decode(frame);
            if (Objects.isNull(header)) {
                log.error("【解码器】Decode error, header is null!");
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
            //String className = inputStream.getClass().getCanonicalName();
            //ObjectUtils.isTrue(log.isDebugEnabled(), "", x -> log.info("【解码器】Decode compression={}, inputStream={}", header.getCompression(), className));

            CommandFactory commandFactory = CommandFactory.match(header.getCommandName());
            @SuppressWarnings("all")
            AbstractCommand<Object> command = (AbstractCommand<Object>) commandFactory.getClazz().getConstructor().newInstance();
            command.setHeader(header);
            Object obj = deserialize(serialization, inputStream, commandFactory);
            command.setCommandBody(obj);
            //ObjectUtils.isTrue(log.isDebugEnabled(), "", x -> log.info("【解码器】Decode success, command={}, type={}", JSONUtil.bean2Json(command), command.getClass().getCanonicalName()));
            return command;
        } catch (Exception ex) {
            log.error("【解码器】Decode exception, message={}", ex.getMessage(), ex);
            NettyAttrManager.closeChannel(ctx.channel());
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
     * @param serialization  序列化
     * @param inputStream    输入流
     * @param commandFactory 命令工厂
     * @return 反序列化
     */
    private Object deserialize(Serialization serialization, InputStream inputStream, CommandFactory commandFactory) {
        return serialization.getSerializer().deserialize(inputStream, getPayloadClass(commandFactory));
    }

    /**
     * 获取命令的消息体类型
     *
     * @param type 命令类型
     * @return 包体类
     */
    protected Class<?> getPayloadClass(final CommandFactory type) {
        return (Class<?>) ((ParameterizedType) type.getClazz().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
