package com.effective.common.netty.cluster.constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BrokerProperties {

    public static final String CONFIGURATION_PREFIX = "broker";

    @Builder.Default
    private int serverPort = 20201;

    @Builder.Default
    private boolean epoll = true;

    @Builder.Default
    private long compressMaxSize = 4069;

    @Builder.Default
    private long timeoutMills = 3000L;

    @Builder.Default
    private int selectorThreadNum = 4;

    @Builder.Default
    private int ioThreadNum = 8;

    @Builder.Default
    private int tcpBackLog = 1024;

    @Builder.Default
    private boolean tcpNoDelay = false;

    @Builder.Default
    private boolean tcpReuseAddr = true;

    @Builder.Default
    private boolean tcpKeepAlive = true;

    @Builder.Default
    private int tcpSndBuf = 65536;

    @Builder.Default
    private int tcpRcvBuf = 65536;

    @Builder.Default
    private boolean pooledByteBufAllocatorEnable = true;

}
