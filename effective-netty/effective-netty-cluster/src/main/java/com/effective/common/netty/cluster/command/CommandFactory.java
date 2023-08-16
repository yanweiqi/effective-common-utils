package com.effective.common.netty.cluster.command;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CommandFactory {

    private static Map<String, CommandHandler<Command>> handlerMap = new ConcurrentHashMap<>();

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

    public static CommandHandler<Command> getHandler(String commandName) {
        return handlerMap.get(commandName);
    }
}
