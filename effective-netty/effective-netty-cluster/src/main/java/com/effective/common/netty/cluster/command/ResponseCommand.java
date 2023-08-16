package com.effective.common.netty.cluster.command;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Response Command
 *
 * <p>The content of the command is the consumption status of the message. 1: success; 0: fail</p>
 *
 * @date 11/2/2020
 */
@Slf4j
@Accessors(chain = true)
public class ResponseCommand extends BaseCommand<Integer> {

    public static String COMMAND_NAME = "responseCommand";

    private int messageId;

    private String brokerId;

    public ResponseCommand() {
        super(COMMAND_NAME);
    }
}
