package com.effective.common.netty.base.frame.codec.messagepack;

import com.effective.common.netty.base.frame.codec.serializable.UserInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Logger;

public class EchoClient {

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new LoggingHandler(LogLevel.INFO))//启动输出info日志
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline p =  channel.pipeline();
                            p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            p.addLast("msgpack decoder", new MsgPackDecoder());
                            /**
                             * 在MessagePack编码器之前增加LengthFiledPrepender，它将在ByteBuf之前增加2个字节的消息长度，其原理就是
                             * +--------------+     +---------+--------------+
                             * | "hello,world"| --> | 0x000c  | "hello,world"|
                             * +--------------+     +---------+--------------+
                             */
                            p.addLast("frameEncoder", new LengthFieldPrepender(2));
                            p.addLast("msgpack encoder", new MsgPackEncoder());
                            p.addLast(new TimeClientHandler());
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

    class TimeClientHandler extends ChannelDuplexHandler {

        private final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            for (int i = 0; i < 10; i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserID(i);
                userInfo.setUserName("ABCDEFG ---> " + i);
                ctx.write(userInfo);
            }
            ctx.flush();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println("client receive message pack :" + msg);
            //ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx){
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // 释放资源
            logger.warning("Unexpected exception from downstream : " + cause.getMessage());
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
        new EchoClient().connect(port, "127.0.0.1");
    }
}
