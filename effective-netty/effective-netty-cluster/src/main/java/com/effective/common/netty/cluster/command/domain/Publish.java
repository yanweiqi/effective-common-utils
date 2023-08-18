package com.effective.common.netty.cluster.command.domain;


import com.effective.common.netty.cluster.command.api.AbstractCommand;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Publish Message Command
 */
@Slf4j
@Accessors(chain = true)
public class Publish<T> extends AbstractCommand<T> {

    /**
     * 发布命令
     */
    public static String COMMAND_NAME = "publishMessage";

    public Publish() {
        super(COMMAND_NAME);
    }
}
