package com.effective.common.netty.cluster.command;


import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Publish Message Command
 *
 * @date 12/2/2020
 */
@Slf4j
@Accessors(chain = true)
public class PublishMessageCommand<T> extends BaseCommand<T> {

    public static String COMMAND_NAME = "publishMessage";

    public PublishMessageCommand() {
        super(COMMAND_NAME);
    }
}
