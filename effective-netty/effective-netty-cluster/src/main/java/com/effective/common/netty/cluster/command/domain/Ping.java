package com.effective.common.netty.cluster.command.domain;


import com.effective.common.netty.cluster.command.api.AbstractCommand;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Accessors(chain = true)
public class Ping extends AbstractCommand<Integer> {

    public static String COMMAND_NAME = "pingCommand";

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 机器id
     */
    private String brokerId;


    public Ping() {
        super(COMMAND_NAME);
    }
}
