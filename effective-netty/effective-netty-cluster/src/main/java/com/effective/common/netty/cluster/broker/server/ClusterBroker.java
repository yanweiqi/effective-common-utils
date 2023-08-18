package com.effective.common.netty.cluster.broker.server;


import com.effective.common.netty.cluster.channel.common.FactoryCommandHandler;
import com.effective.common.netty.cluster.channel.server.LifeCycleHandler;
import com.effective.common.netty.cluster.channel.server.ServerConnectionHandler;
import com.effective.common.netty.cluster.channel.server.ServerIdleCheckHandler;
import com.effective.common.netty.cluster.command.factory.CommandFactory;
import com.effective.common.netty.cluster.constants.BrokerProperties;
import com.effective.common.netty.cluster.transport.codec.CommandDecoder;
import com.effective.common.netty.cluster.transport.codec.CommandEncoder;
import com.effective.common.netty.cluster.utils.IpUtil;
import com.effective.common.netty.cluster.utils.SystemUtil;
import com.effective.common.netty.cluster.utils.factory.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Cluster Broker
 *
 * @date 2/2/2020
 */
@Slf4j
public class ClusterBroker extends AbstractBroker {

    private static volatile boolean running = false;

    private final EventLoopGroup selectorGroup;

    private final EventLoopGroup ioGroup;

    private final Class<? extends ServerChannel> clazz;

    private final ServerBootstrap serverBootstrap;

    private final BrokerProperties brokerProperties;

    private Channel serverChannel;

    public ClusterBroker(BrokerProperties brokerProperties) {
        this.brokerProperties = brokerProperties;
        if (brokerProperties.isEpoll() && SystemUtil.isLinux()) {
            selectorGroup = new EpollEventLoopGroup(brokerProperties.getSelectorThreadNum(), new NamedThreadFactory("selectorEventGroup"));
            ioGroup = new EpollEventLoopGroup(brokerProperties.getIoThreadNum(), new NamedThreadFactory("iOEventGroup"));
            clazz = EpollServerSocketChannel.class;
        } else {
            selectorGroup = new NioEventLoopGroup(brokerProperties.getSelectorThreadNum(), new NamedThreadFactory("selectorEventGroup"));
            ioGroup = new NioEventLoopGroup(brokerProperties.getIoThreadNum(), new NamedThreadFactory("iOEventGroup"));
            clazz = NioServerSocketChannel.class;
        }
        this.serverBootstrap = new ServerBootstrap();
    }

    public void start() {
        this.serverBootstrap.group(selectorGroup, ioGroup)
                .channel(clazz)
                .childOption(ChannelOption.TCP_NODELAY, brokerProperties.isTcpNoDelay())
                .option(ChannelOption.SO_REUSEADDR, brokerProperties.isTcpReuseAddr())
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                .childOption(ChannelOption.SO_KEEPALIVE, brokerProperties.isTcpKeepAlive())
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("lifeCycleHandler", new LifeCycleHandler())//inbound
                                .addLast("serverIdleHandler", new ServerIdleCheckHandler())
                                .addLast("commandDecoder", new CommandDecoder()) //inbound
                                .addLast("commandEncoder", new CommandEncoder()) //outbound
                                .addLast("serverConnectionHandler", new ServerConnectionHandler()) //inbound
                                .addLast("factoryCommandHandler", new FactoryCommandHandler());  //inbound
                        //.addLast("commonHandler", new BaseCommandHandler()); //inbound
                    }
                });
        if (brokerProperties.isPooledByteBufAllocatorEnable()) {
            this.serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
        try {
            serverChannel = this.serverBootstrap.bind(brokerProperties.getServerPort()).sync().channel();
            log.info("【服务端】启动成功,ip={}, port={}", IpUtil.getLocalIp(), brokerProperties.getServerPort());
        } catch (Exception ex) {
            log.error("【服务端】启动失败! cause={}", ex.getMessage(), ex);
        }
        running = true;
    }

    public void stop() {
        log.info("Stopping broker!");
        try {
            if (Objects.nonNull(serverChannel) && serverChannel.isActive() && serverChannel.isOpen()) {
                serverChannel.close().sync();
            }
        } catch (Exception e) {
            log.error("Stop broker error:{}", e.getMessage(), e);
        } finally {
            selectorGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
        log.info("Broker stopped!");
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args) {
        ClusterBroker clusterBroker = new ClusterBroker(BrokerProperties.builder().serverPort(8090).build());
        clusterBroker.start();

        CommandFactory.loader();
    }
}
