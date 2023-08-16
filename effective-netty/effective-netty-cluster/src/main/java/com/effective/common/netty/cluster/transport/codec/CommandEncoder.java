package com.effective.common.netty.cluster.transport.codec;


import com.effective.common.netty.cluster.command.BaseCommand;
import com.effective.common.netty.cluster.transport.compression.AdaptiveCompressOutputStream;
import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.compression.CompressionSelector;
import com.effective.common.netty.cluster.transport.protocol.CommandProtocol;
import com.effective.common.netty.cluster.transport.protocol.Protocol;
import com.effective.common.netty.cluster.transport.serialization.Serialization;
import com.effective.common.netty.cluster.transport.serialization.SerializationSelector;
import com.effective.common.netty.cluster.utils.io.ChannelUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;

/**
 * Command encoder
 */
@Slf4j
public class CommandEncoder extends MessageToByteEncoder<BaseCommand> {

    private final Protocol protocol = new CommandProtocol();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseCommand baseCommand, ByteBuf outByteBuf) throws Exception {
        try {
            if (outByteBuf != null) {
                // Write magic code
                outByteBuf.writeBytes(protocol.getMagicCode());
                int begin = outByteBuf.writerIndex();
                // 返回压缩标志位置，标记压缩类型
                int compressIndex = baseCommand.getHeader().encode(outByteBuf);

                if (baseCommand.getCommandBody() != null) {
                    try (OutputStream os = new ByteBufOutputStream(outByteBuf)) {
                        Serialization serialization = SerializationSelector.select(baseCommand.getHeader().getSerialization());
                        if (baseCommand.getHeader().getCompression() > 0) {
                            Compression compression = CompressionSelector.select(baseCommand.getHeader().getCompression());
                            if (compression != null) {
                                //Adaptive Compress
                                AdaptiveCompressOutputStream acos = new AdaptiveCompressOutputStream(outByteBuf, compression);
                                serialize(serialization, acos, baseCommand);
                                acos.finish();
                                acos.flush();
                                outByteBuf.setByte(compressIndex, !acos.isCompressed() ? Compression.NONE : baseCommand.getHeader().getCompression());
                                if (log.isDebugEnabled()) {
                                    log.debug("Encode isCompression={}, compressor={}", acos.isCompressed(), compression.getClass().getCanonicalName());
                                }
                            } else {
                                outByteBuf.setByte(compressIndex, Compression.NONE);
                            }
                        } else {
                            serialize(serialization, os, baseCommand);
                        }
                    } catch (Exception e) {
                        log.error("Command Encode error! message={}", e.getMessage(), e);
                    }
                }
                int end = outByteBuf.writerIndex();
                int size = end - begin;
                baseCommand.getHeader().setLength(size);
                outByteBuf.writerIndex(begin);
                outByteBuf.writeInt(size);
                outByteBuf.writerIndex(end);
            }
        } catch (Exception ex) {
            log.error("[###publishMessageCommandEncoder###]Encode sync message error, baseCommand={}", baseCommand);
            ChannelUtil.closeChannel(channelHandlerContext.channel());
        }
    }

    /**
     * Serialize
     *
     * @param serialization Serialization
     * @param os            OutputStream
     * @param baseCommand   Command
     */
    protected void serialize(final Serialization serialization, final OutputStream os, final BaseCommand baseCommand) {
        serialization.getSerializer().serialize(os, baseCommand.getCommandBody());
    }
}
