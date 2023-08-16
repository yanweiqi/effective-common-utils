package com.effective.common.netty.cluster.utils;

/**
 * 网段
 */
public class Segment {

    /**
     * 掩码位数(1-32)对应的子网掩码
     */
    public static final String[] MASKES =
            new String[]{"128.0.0.0", "192.0.0.0", "224.0.0.0", "240.0.0.0", "248.0.0.0", "252.0.0.0", "254.0.0.0",
                    "255.0.0.0", "255.128.0.0", "255.192.0.0", "255.224.0.0", "255.240.0.0", "255.248.0.0",
                    "255.252.0.0", "255.254.0.0", "255.255.0.0", "255.255.128.0", "255.255.192.0", "255.255.224.0",
                    "255.255.240.0", "255.255.248.0", "255.255.252.0", "255.255.254.0", "255.255.255.0",
                    "255.255.255.128", "255.255.255.192", "255.255.255.224", "255.255.255.240", "255.255.255.248",
                    "255.255.255.252", "255.255.255.254", "255.255.255.255"};

    // 起始IP
    private long begin;
    // 最后IP
    private long end;

    public Segment(final String ips) {
        if (ips == null || ips.isEmpty()) {
            throw new IllegalArgumentException("ips is empty.");
        }
        int pos = ips.indexOf('-');
        if (pos == 0 || pos == ips.length() - 1) {
            throw new IllegalArgumentException(String.format("ips is invalid. %s", ips));
        } else if (pos > 0) {
            // IP-IP格式
            begin = IpUtil.toLong(ips.substring(0, pos));
            end = IpUtil.toLong(ips.substring(pos + 1));
        } else {
            pos = ips.indexOf('/');
            if (pos == 0 || pos == ips.length() - 1) {
                throw new IllegalArgumentException(String.format("ips is invalid. %s", ips));
            } else if (pos > 0) {
                // IP/掩码格式
                int bits = Integer.parseInt(ips.substring(pos + 1));
                if (bits < 1 || bits > 32) {
                    throw new IllegalArgumentException(String.format("ips is invalid. %s", ips));
                }
                long mask = (int) IpUtil.toLong(MASKES[bits - 1]);
                begin = IpUtil.toLong(ips.substring(0, pos)) & mask;
                end = begin + ~((int) mask);
            } else {
                // 可能存在*号
                begin = IpUtil.toLong(ips.replaceAll("\\*", "0"));
                end = IpUtil.toLong(ips.replaceAll("\\*", "255"));
            }
        }
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Segment segment = (Segment) o;

        if (begin != segment.begin) {
            return false;
        }
        return end == segment.end;

    }

    @Override
    public int hashCode() {
        int result = (int) (begin ^ (begin >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return IpUtil.toIp(begin) + "-" + IpUtil.toIp(end);
    }

    /**
     * 是否包含指定IP
     *
     * @param ip IP
     * @return 布尔值
     */
    public boolean contains(final String ip) {
        long value = IpUtil.toLong(ip);
        return value >= begin && value <= end;
    }
}
