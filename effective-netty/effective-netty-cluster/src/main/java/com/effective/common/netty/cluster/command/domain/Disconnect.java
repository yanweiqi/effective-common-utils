package com.effective.common.netty.cluster.command.domain;


import com.effective.common.netty.cluster.command.api.AbstractCommand;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Accessors(chain = true)
public class Disconnect extends AbstractCommand<String> {

    public static String COMMAND_NAME = "disconnectCommand";

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 机器id
     */
    private String brokerId;


    public Disconnect() {
        super(COMMAND_NAME);
    }

}
