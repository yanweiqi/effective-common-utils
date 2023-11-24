package com.effective.common.netty.cluster.command.factory;


import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.command.domain.Disconnect;
import com.effective.common.netty.cluster.command.domain.Ping;
import com.effective.common.netty.cluster.command.domain.Pong;
import com.effective.common.netty.cluster.handler.CommandHandler;
import com.effective.common.netty.cluster.handler.PingCommandHandler;
import com.effective.common.netty.cluster.handler.PongCommandHandler;
import com.effective.common.netty.cluster.utils.reflect.ClazzUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public enum CommandFactory {

    /**
     * 心跳命令
     */
    PING_MESSAGE(Ping.COMMAND_NAME, Ping.class) {
        @Override
        @SuppressWarnings("all")
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(PingCommandHandler.class.getSimpleName());
        }
    },

    /**
     * 心跳命令
     */
    PONG_MESSAGE(Pong.COMMAND_NAME, Pong.class) {
        @Override
        @SuppressWarnings("all")
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(PongCommandHandler.class.getSimpleName());
        }
    },

    /**
     * 断开连接命令
     */
    DISCONNECT_MESSAGE(Disconnect.COMMAND_NAME, Disconnect.class) {
        @Override
        @SuppressWarnings("all")
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(Disconnect.class.getSimpleName());
        }
    };

    /**
     * 命令名称
     */
    private final String commandName;

    /**
     * 命令类
     */
    private final Class<? extends Command> clazz;

    /**
     * 获取命令名称
     *
     * @return string
     */
    @SuppressWarnings("all")
    public String getCommandName() {
        return commandName;
    }

    /**
     * 获取class
     *
     * @return class
     */
    public Class<? extends Command> getClazz() {
        return clazz;
    }

    /**
     * 命令执行容器
     */
    @SuppressWarnings("all")
    public final static Map<String, CommandHandler> handlerMap = new ConcurrentHashMap<>();


    /**
     * 初始化实现类
     */
   public static void loader() {
        ClazzUtils.loadApiImplClass(CommandHandler.class, CommandHandler.class.getPackage().getName(), CommandFactory.handlerMap);
    }

    /**
     * @param commandName 命令名称
     * @param clazz       类
     */
    CommandFactory(String commandName, Class<? extends Command> clazz) {
        this.commandName = commandName;
        this.clazz = clazz;
    }

    /**
     * 获取命令执行器
     *
     * @return 命令执行器
     */
    public abstract CommandHandler<Command> getHandler();

    /**
     * 匹配命令执行工厂
     *
     * @param commandName 执行器名称
     * @return 命令执行器工厂
     */
    public static CommandFactory match(@NonNull String commandName) {
        for (CommandFactory type : CommandFactory.values()) {
            if (commandName.equalsIgnoreCase(type.commandName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("type do not supported");
    }

}
