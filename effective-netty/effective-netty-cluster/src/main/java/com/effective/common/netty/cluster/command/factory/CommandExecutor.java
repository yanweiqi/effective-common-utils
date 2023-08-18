package com.effective.common.netty.cluster.command.factory;

import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.handler.CommandHandler;
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

    /**
     * 命令执行线程
     *
     * @param handler 执行器
     * @param command 命  令
     */
    public CommandExecutor(final CommandHandler<Command> handler, final Command command) {
        this.handler = handler;
        this.command = command;
    }

    @Override
    public void run() {
        try {
            if (handler != null) {
                handler.execute(command);
            } else {
                log.error("找不到命令执行器 commandName:{}", command.getCommandName());
            }
        } catch (Throwable th) {
            log.error("命令执行线程异常，{}",th.getMessage(), th);
        }
    }
}
