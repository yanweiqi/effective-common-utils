package com.effective.common.netty.base.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by yanweiqi on 2018/12/4.
 */
public class TimeClient {

    private static BlockingQueue<byte[]> sendQ = new LinkedBlockingDeque<>();

    private static BlockingQueue<Object> receQ = new LinkedBlockingDeque<>();


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    Object obj = receQ.take();
                    if (obj != null) {
                        ByteBuf buf = (ByteBuf) obj;
                        byte[] req = new byte[buf.readableBytes()];
                        buf.readBytes(req);
                        String body = new String(req, "UTF-8");
                        System.out.println("Now is : " + body);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                byte[] req = "QUERY TIME ORDER".getBytes();
                sendQ.offer(req);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
        t1.start();
        startUp(args);
    }

    private static void startUp(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new TimeClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeClientHandler());
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
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            try {
                byte[] req = sendQ.take();
                if (req != null) {
                    final ByteBuf firstMessage = Unpooled.buffer(req.length);
                    firstMessage.writeBytes(req);
                    ctx.writeAndFlush(firstMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            receQ.offer(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    }
}