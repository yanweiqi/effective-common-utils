package com.effective.common.netty.cluster.command;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CommandFactory {

    /**
     * 命令执行容器
     */
    private static Map<String, CommandHandler<Command>> handlerMap = new ConcurrentHashMap<>();

    /**
     * 添加命令执行器
     *
     * @param commandHandler 命令执行器
     */
    public static void addHandler(CommandHandler<Command> commandHandler) {
        commandHandler.getApplyCommands().forEach(commandName -> {
            if (!handlerMap.containsKey(commandName)) {
                handlerMap.computeIfAbsent(commandName, s -> commandHandler);
            } else {
                log.error("CommandName[{}] has duplicate handlers. This handler will be ignored (invalid)! handler class={}",
                        commandName, commandHandler.getClass().getCanonicalName());
            }
        });
    }

    /**
     * 获取命令执行器
     * @param commandName 执行器名称
     * @return 命令执行器
     */
    public static CommandHandler<Command> getHandler(String commandName) {
        return handlerMap.get(commandName);
    }
}
