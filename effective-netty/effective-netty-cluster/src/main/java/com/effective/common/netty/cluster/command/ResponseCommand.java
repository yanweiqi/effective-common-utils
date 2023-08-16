package com.effective.common.netty.cluster.command;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(chain = true)
public class ResponseCommand extends BaseCommand<Integer> {

    public static String COMMAND_NAME = "responseCommand";

    /**
     * 消息id
     */
    private int messageId;

    /**
     * 机器id
     */
    private String brokerId;

    public ResponseCommand() {
        super(COMMAND_NAME);
    }
}
