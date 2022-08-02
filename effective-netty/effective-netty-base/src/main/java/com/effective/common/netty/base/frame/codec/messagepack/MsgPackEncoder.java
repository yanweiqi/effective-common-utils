package com.effective.common.netty.base.frame.codec.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class MsgPackEncoder  extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        MessagePack mk = new MessagePack();
        byte[] raw = mk.write(obj);
        byteBuf.writeBytes(raw);
    }
}