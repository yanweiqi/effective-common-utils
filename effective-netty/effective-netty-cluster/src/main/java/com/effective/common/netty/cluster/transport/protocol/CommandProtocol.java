package com.effective.common.netty.cluster.transport.protocol;

/**
 * Command Protocol
 */
public class CommandProtocol implements Protocol {

    public static final byte[] MAGIC_CODE = new byte[]{(byte) 0xC0, (byte) 0xD0};

    @Override
    public byte[] getMagicCode() {
        return MAGIC_CODE;
    }
}
