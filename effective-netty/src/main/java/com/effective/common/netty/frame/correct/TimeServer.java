package com.effective.common.netty.frame.correct;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.commons.lang3.CharSet;

import java.nio.charset.Charset;

/**
 * 粘包问题的解决策略
 * <p>
 * 由于底层的TCP无法理解上层的业务数据，所以在底层是无法保证数据包不被拆分和重组，这个问题只能通过上层的应用
 * 协议栈设计来解决，根据业界的主流协议的解决方案，可以归纳如下
 * <p>
 * 1) 消息定长，例如每个报文的大小为固定200字节，如果不够，空位补空格
 * <p>
 * 2) 在包尾部增加回车换行符进行分割，例如FTP协议
 * <p>
 * 3) 将消息分为消息头和消息体，消息头中包含消息总长度的字段
 * <p>
 * 4) 更复杂的应用协议
 */
public class TimeServer {

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    /**
                     * 创建serverSocketChannel
                     */
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 存放已完成三次握手的请求的等待队列的最大长度
                     * backlog设置为1024
                     */
                    .option(ChannelOption.SO_BACKLOG, 1024)

                    /**
                     *
                     */
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
        protected void initChannel(SocketChannel channel) throws Exception {

            channel.pipeline()
                    /**
                     * 解决粘包的解码器
                     * LineBasedFrameDecoder它依次遍历ByteBuf中可读字节，判断是否有\r\n
                     * ,如果有就以此位置为结束位置，从可读索引到结束位置区间的字节就组成了一行。它是以换行符
                     * 为结束标志的解码器，支持携带结束符或者不携带结束符两种解码方式，同时支持配置单行的最大长度
                     * 如果连续读到最大长度还没有发现换行符就会抛出异常同时忽略之前读到的异常码流
                     */
                    .addLast(new LineBasedFrameDecoder(1024))

                    /**
                     * StringDecode的功能非常的简单,把接收到数据流转成字符串
                     * 然后继续调用后面的handler.
                     * LineBasedFrameDecoder + StringDecode组合就是按行
                     * 切换文本解码器
                     *
                     */
                    //.addLast(new StringDecoder())
                    .addLast(new TimeServerHandler());

        }
    }

    class TimeServerHandler extends ChannelHandlerAdapter {

        private int counter;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            /**
             * 如果使用StringDecoder可以直接把msg转成字符串
             */
            //String body = (String) msg;

            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, "utf-8");

            System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            currentTime = currentTime + System.getProperty("line.separator");
            ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.writeAndFlush(resp);
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
        new TimeServer().bind(port);
    }
}
