package com.effective.common.netty.cluster.broker.server;


import com.effective.common.netty.cluster.constants.BrokerProperties;
import com.effective.common.netty.cluster.constants.GatewayConstants;
import com.effective.common.netty.cluster.handler.BaseCommandHandler;
import com.effective.common.netty.cluster.handler.MessageSyncCommandHandler;
import com.effective.common.netty.cluster.handler.ServerConnectionHandler;
import com.effective.common.netty.cluster.handler.ServerIdleCheckHandler;
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
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Cluster Broker
 *
 * @date 2/2/2020
 */
@Slf4j
//@Component
//@Configuration
//@EnableConfigurationProperties(BrokerProperties.class)
public class ClusterBroker extends AbstractBroker {

    private static volatile boolean running = false;

    private EventLoopGroup selectorGroup;

    private EventLoopGroup ioGroup;

    private Class<? extends ServerChannel> clazz;

    private ServerBootstrap serverBootstrap;

    private final BrokerProperties brokerProperties;

    private Channel serverChannel;

//    @Autowired
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

    //@Override
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
                        pipeline.addLast("serverIdleHandler", new ServerIdleCheckHandler())
                                .addLast("commandDecoder", new CommandDecoder())
                                .addLast("commandEncoder", new CommandEncoder())
                                .addLast("serverConnectionHandler", new ServerConnectionHandler())
                                .addLast("commandServerHandler", new MessageSyncCommandHandler())
                                .addLast("commonHandler", new BaseCommandHandler()
                                );
                    }
                });
        if (brokerProperties.isPooledByteBufAllocatorEnable()) {
            this.serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
        try {
            serverChannel = this.serverBootstrap.bind(brokerProperties.getServerPort()).sync().channel();
            log.info("{} {} BrokerServer starting success,ip={}, port={}",
                    GatewayConstants.SYSTEM_NAME,
                    GatewayConstants.SYSTEM_MQTT_START,
                    IpUtil.getLocalIp(),
                    brokerProperties.getServerPort());
        } catch (Exception ex) {
            log.error("{} {} broker Starting failure! cause={}",
                    GatewayConstants.SYSTEM_NAME,
                    GatewayConstants.SYSTEM_MQTT_START,
                    ex.getMessage(), ex);
        }
        running = true;
    }

    //@Override
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

    //@Override
    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args) {
        ClusterBroker clusterBroker = new ClusterBroker(BrokerProperties.builder().serverPort(8090).build());
        clusterBroker.start();
    }
}
