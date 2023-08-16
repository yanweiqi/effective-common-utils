package com.effective.common.netty.cluster.constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrokerProperties {

    public static final String CONFIGURATION_PREFIX = "broker";

    private int serverPort = 20201;

    private boolean epoll = true;

    private long compressMaxSize = 4069;

    private long timeoutMills = 3000L;

    private int selectorThreadNum = 4;

    private int ioThreadNum = 8;

    private int tcpBackLog = 1024;

    private boolean tcpNoDelay = false;

    private boolean tcpReuseAddr = true;

    private boolean tcpKeepAlive = true;

    private int tcpSndBuf = 65536;

    private int tcpRcvBuf = 65536;

    private boolean pooledByteBufAllocatorEnable = true;

}
