package com.effective.common.netty.cluster.command.domain;

import com.effective.common.netty.cluster.command.api.AbstractCommand;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(chain = true)
public class Response extends AbstractCommand<Integer> {

    /**
     * 命令名称
     */
    public static String COMMAND_NAME = "responseCommand";

    /**
     * 消息id
     */
    private int messageId;

    /**
     * 机器id
     */
    private String brokerId;

    public Response() {
        super(COMMAND_NAME);
    }
}
