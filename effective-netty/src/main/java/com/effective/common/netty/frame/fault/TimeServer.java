package com.effective.common.netty.frame.fault;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 数据粘包的问题
 * <p>
 * 一、加入客户端发送两个数据包
 * Client  -------->  Server
 * <p>
 * 1)  [D2]......[D1]      服务端分两次读到了两个独立的数据包，分别D1和D2
 * <p>
 * 2)  ......[D2][D1]      服务端一次收到了两个数据包，D1+D2粘在一起
 * <p>
 * 3)  [D2_2]..[D2_1][D1]  服务端分两次读到了两个数据包，D2_2和D2_1+D1数据包
 * <p>
 * 4)  [D2][D1_2]...[D1_1] 服务端分两次读到了两个数据包，D2+D1_2和D1_1
 * <p>
 * 如果此时服务端TCP接收滑动窗口非常小，而数据包D1和D2比较大，很有可能会出现第5种情况，服务端分多次
 * 才能将D1和D2包完全接收完全。
 * <p>
 * 二、出现粘包的原因分析
 * 1) 应用程序write写入的字节大小 > 套接口发送缓冲区大小
 * 2) 进行MSS大小的TCP分段
 * 3) 以太网帧的payload大于MTU进行IP分片
 */
public class TimeServer {

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
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
            channel.pipeline().addLast(new TimeServerHandler());
        }
    }

    class TimeServerHandler extends ChannelDuplexHandler {

        private int counter;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());
            System.out.println(" the counter is : " + ++counter + ";The time server receive order : " + body);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                    new java.util.Date(System.currentTimeMillis()).toString() :
                    "BAD ORDER";
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
