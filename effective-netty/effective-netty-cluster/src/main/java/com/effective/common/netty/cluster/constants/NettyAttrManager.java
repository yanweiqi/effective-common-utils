package com.effective.common.netty.cluster.constants;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
public class NettyAttrManager {

    public static final String CLIENT_ID = "clientId";

    public static final String CLEAN_SESSION = "cleanSession";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String KEEP_ALIVE = "keepAlive";

    private static final AttributeKey<Object> ATTR_KEY_KEEPALIVE = AttributeKey.valueOf(KEEP_ALIVE);

    private static final AttributeKey<Object> ATTR_KEY_CLEAN_SESSION = AttributeKey.valueOf(CLEAN_SESSION);

    private static final AttributeKey<Object> ATTR_KEY_CLIENT_ID = AttributeKey.valueOf(CLIENT_ID);

    private static final AttributeKey<Object> ATTR_KEY_USERNAME = AttributeKey.valueOf(USERNAME);

    private static final AttributeKey<Object> ATTR_KEY_PASSWORD = AttributeKey.valueOf(PASSWORD);

    public static String getAttrClientId(Channel channel) {
        return (String) channel.attr(NettyAttrManager.ATTR_KEY_CLIENT_ID).get();
    }

    public static void setAttrClientId(Channel channel, String clientId) {
        channel.attr(NettyAttrManager.ATTR_KEY_CLIENT_ID).set(clientId);
    }

    public static Boolean getAttrCleanSession(Channel channel) {
        return (Boolean) channel.attr(NettyAttrManager.ATTR_KEY_CLEAN_SESSION).get();
    }

    public static void setAttrCleanSession(Channel channel, Boolean cleanSession) {
        channel.attr(NettyAttrManager.ATTR_KEY_CLEAN_SESSION).set(cleanSession);
    }

    public static int getAttrKeepAlive(Channel channel) {
        return (int) channel.attr(NettyAttrManager.ATTR_KEY_KEEPALIVE).get();
    }

    public static void setAttrKeepAlive(Channel channel, int keepAlive) {
        channel.attr(NettyAttrManager.ATTR_KEY_KEEPALIVE).set(keepAlive);
    }

    public static void setUsername(Channel channel, String username) {
        channel.attr(NettyAttrManager.ATTR_KEY_USERNAME).set(username);
    }

    public static String getUsername(Channel channel) {
        return (String) channel.attr(NettyAttrManager.ATTR_KEY_USERNAME).get();
    }

    public static void setPassword(Channel channel, String password) {
        channel.attr(NettyAttrManager.ATTR_KEY_PASSWORD).set(password);
    }

    public static String getPassword(Channel channel) {
        return (String) channel.attr(NettyAttrManager.ATTR_KEY_PASSWORD).get();
    }

    /**
     *
     *
     * @param channel io
     * @return String
     */
    public static String getRemoteAdd(Channel channel) {
        if (Objects.isNull(channel)) return "";
        SocketAddress remote = channel.remoteAddress();
        final String add = remote != null ? remote.toString() : "";
        if (add.length() > 0) {
            int index = add.lastIndexOf("/");
            if (index >= 0) {
                return add.substring(index + 1);
            }
            return add;
        }
        return "";
    }

    /**
     *
     * @param channel
     */
    public static void closeChannel(Channel channel) {
        String remoteAdd = getRemoteAdd(channel);
        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("[###CloseChannel###] -> Close the connection, address={}, result={}", remoteAdd, channelFuture.isSuccess());
            }
        });
    }

    /**
     *
     *
     * @param add 地址
     * @return socketAddress
     */
    public static SocketAddress string2SocketAddress(final String add) {
        String[] s = add.split(":");
        InetSocketAddress isa = new InetSocketAddress(s[0], Integer.parseInt(s[1]));
        return isa;
    }

    /**
     *
     * @return string
     */
    public static String getLocalAdd() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> ipv4Result = new ArrayList<String>();
            ArrayList<String> ipv6Result = new ArrayList<String>();
            while (enumeration.hasMoreElements()) {
                final NetworkInterface networkInterface = enumeration.nextElement();
                final Enumeration<InetAddress> en = networkInterface.getInetAddresses();
                while (en.hasMoreElements()) {
                    final InetAddress address = en.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        } else {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            // prefer ipv4
            if (!ipv4Result.isEmpty()) {
                for (String ip : ipv4Result) {
                    if (ip.startsWith("127.0") || ip.startsWith("192.168")) {
                        continue;
                    }

                    return ip;
                }

                return ipv4Result.get(ipv4Result.size() - 1);
            } else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            }
            //If failed to find,fall back to localhost
            final InetAddress localHost = InetAddress.getLocalHost();
            return normalizeHostAddress(localHost);
        } catch (Exception e) {
            log.error("failed to get local addr", e);
        }
        return null;
    }

    private static String normalizeHostAddress(final InetAddress localHost) {
        if (localHost instanceof Inet6Address) {
            return "[" + localHost.getHostAddress() + "]";
        } else {
            return localHost.getHostAddress();
        }
    }
}
