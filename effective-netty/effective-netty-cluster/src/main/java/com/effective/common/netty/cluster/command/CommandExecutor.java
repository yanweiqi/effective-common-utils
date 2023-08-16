package com.effective.common.netty.cluster.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CommandExecutor implements Runnable {

    /**
     * 命令执行器接口
     */
    private CommandHandler<Command> handler;

    /**
     * 命令
     */
    private Command command;

    public CommandExecutor(final CommandHandler<Command> handler, final Command command) {
        this.handler = handler;
        this.command = command;
    }

    @Override
    public void run() {
        execute();
    }

    public void execute() {
        try {
            if (handler != null) {
                handler.execute(command);
            } else {
                log.error("Cant find the right command handler to execute! commandName:{}", command.getCommandName());
            }
        } catch (Throwable th) {
            log.error("CommandExecutor got exception: ", th);
        }
    }
}
