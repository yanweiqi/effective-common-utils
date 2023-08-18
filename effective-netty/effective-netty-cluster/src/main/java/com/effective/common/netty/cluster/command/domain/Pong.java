package com.effective.common.netty.cluster.command.domain;


import com.effective.common.netty.cluster.command.api.AbstractCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Accessors(chain = true)
@Getter
@Setter
public class Pong extends AbstractCommand<Integer> {

    public static String COMMAND_NAME = "pongCommand";

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 机器id
     */
    private String brokerId;


    public Pong() {
        super(COMMAND_NAME);
    }
}
