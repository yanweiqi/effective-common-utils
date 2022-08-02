package com.effective.common.netty.base.frame.codec.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer, List<Object> list) throws Exception {
        final byte[] array;
        final int length = buffer.readableBytes();
        array = new byte[length];
        buffer.getBytes(buffer.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        list.add(messagePack.read(array));
    }
}
