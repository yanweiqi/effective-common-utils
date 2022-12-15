package com.effective.common.netty.frame.codec.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.ArrayList;
import java.util.List;

public class SubReqClient {

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            channel.pipeline().addLast(new ProtobufDecoder(SubscribeRepProto.SubscribeRep.getDefaultInstance()));
                            channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            channel.pipeline().addLast(new ProtobufEncoder());
                            channel.pipeline().addLast(new SubReqClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            // 当代客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    class SubReqClientHandler extends ChannelDuplexHandler {

        /**
         * Creates a client-side handler.
         */
        public SubReqClientHandler() {
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            for (int i = 0; i < 1; i++) {
                ctx.write(subReq(i));
            }
            ctx.flush();
        }

        private SubscribeReqProto.SubscribeReq subReq(int i) {
            SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
            builder.setSubReqID(1);
            builder.setUserName("Lilinfeng");
            builder.setProductName("Netty Book");
            builder.setPhoneNumber("13811117685");
            List<String> address = new ArrayList<>();
            address.add("NanJing YuHuaTai");
            address.add("BeiJing LiuLiChang");
            address.add("ShenZhen HongShuLin");
            builder.addAllAddress(address);
            return builder.build();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println("Receive server response : [" + msg + "]");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new SubReqClient().connect(port, "127.0.0.1");
    }
}
