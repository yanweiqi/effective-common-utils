package com.effective.common.netty.cluster.command;


import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Accessors(chain = true)
public class DisconnectCommand extends BaseCommand<String> {

    public static String COMMAND_NAME = "disconnectCommand";

    private String clientId;

    private String brokerId;

    public DisconnectCommand() {
        super(COMMAND_NAME);
    }
}
