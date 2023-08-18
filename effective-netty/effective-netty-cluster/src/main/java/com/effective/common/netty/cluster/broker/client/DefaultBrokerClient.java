package com.effective.common.netty.cluster.broker.client;


import com.effective.common.netty.cluster.channel.client.ClientConnectionHandler;
import com.effective.common.netty.cluster.channel.client.ClientIdleCheckHandler;
import com.effective.common.netty.cluster.channel.client.ClientKeepaliveHandler;
import com.effective.common.netty.cluster.channel.common.FactoryCommandHandler;
import com.effective.common.netty.cluster.command.factory.CommandFactory;
import com.effective.common.netty.cluster.constants.BrokerProperties;
import com.effective.common.netty.cluster.constants.NettyAttrManager;
import com.effective.common.netty.cluster.transport.codec.CommandDecoder;
import com.effective.common.netty.cluster.transport.codec.CommandEncoder;
import com.effective.common.netty.cluster.utils.SystemUtil;
import com.effective.common.netty.cluster.utils.factory.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Slf4j
public class DefaultBrokerClient extends AbstractBrokerClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup ioGroup;

    private final String address;

    public DefaultBrokerClient(BrokerProperties brokerProperties, String address) {
        this.address = address;
        Class<? extends SocketChannel> clazz;
        if (!brokerProperties.isEpoll() && SystemUtil.isLinux()) {
            ioGroup = new EpollEventLoopGroup(brokerProperties.getIoThreadNum(), new NamedThreadFactory("broker-client-pool"));
            clazz = EpollSocketChannel.class;
        } else {
            ioGroup = new NioEventLoopGroup(brokerProperties.getIoThreadNum(), new NamedThreadFactory("broker-client-pool"));
            clazz = NioSocketChannel.class;
        }
        bootstrap = new Bootstrap();
        bootstrap.group(ioGroup)
                .channel(clazz)
                .option(ChannelOption.SO_REUSEADDR, brokerProperties.isTcpReuseAddr())
                .option(ChannelOption.SO_KEEPALIVE, brokerProperties.isTcpKeepAlive())
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("clientIdleHandler", new ClientIdleCheckHandler())
                                .addLast("commandEncoder", new CommandEncoder())
                                .addLast("commandDecoder", new CommandDecoder())
                                .addLast("clientConnectionHandler", new ClientConnectionHandler(DefaultBrokerClient.this::disconnect))
                                .addLast("factoryCommandHandler", new FactoryCommandHandler())
                                .addLast("keepaliveHandler", new ClientKeepaliveHandler());
                    }
                });
        if (brokerProperties.isPooledByteBufAllocatorEnable()) {
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
        bootstrap.remoteAddress(NettyAttrManager.string2SocketAddress(address));
    }

    @Override
    public void connect() throws InterruptedException {
        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            this.setChannel(channelFuture.channel());
            running = true;
        } catch (Exception e) {
            running = false;
            log.error("Connect to broker error! message={}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void disconnect() {
        if (log.isInfoEnabled()) {
            log.info("【客户端】链接被断开! remote server address={}", address);
        }
        if (Objects.nonNull(getChannel()) && getChannel().isOpen()) {
            getChannel().disconnect();
        }
        if (ioGroup != null) {
            log.info("【客户端】优雅关闭，释放端口");
            ioGroup.shutdownGracefully();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args) throws InterruptedException {
        DefaultBrokerClient brokerClient = new DefaultBrokerClient(BrokerProperties.builder().serverPort(8090).build(), "127.0.0.1:8090");
        brokerClient.connect();
        CommandFactory.loader();
    }


}
