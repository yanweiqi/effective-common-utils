package com.effective.common.netty.base.frame.codec.messagepack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 粘包问题的解决策略
 *
 * 由于底层的TCP无法理解上层的业务数据，所以在底层是无法保证数据包不被拆分和重组，这个问题只能通过上层的应用
 * 协议栈设计来解决，根据业界的主流协议的解决方案，可以归纳如下
 *
 *  1) 消息定长，例如每个报文的大小为固定200字节，如果不够，空位补空格
 *
 *  2) 在包尾部增加回车换行符进行分割，例如FTP协议
 *
 *  3) 将消息分为消息头和消息体，消息头中包含消息总长度的字段
 *
 *  4) 更复杂的应用协议
 */
public class EchoServer {

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))//启动输出info日志
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) {
            ChannelPipeline p = channel.pipeline();
            /**
             * 此处为了解决粘包问题
             */
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
            p.addLast(new TimeServerHandler());
        }
    }

    class TimeServerHandler extends ChannelDuplexHandler {

        public TimeServerHandler() {
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println("Server receive the msgpack message : " + msg);
            ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
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
        new EchoServer().bind(port);
    }
}
