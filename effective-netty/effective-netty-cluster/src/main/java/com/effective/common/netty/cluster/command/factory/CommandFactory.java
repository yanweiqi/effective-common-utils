package com.effective.common.netty.cluster.command.factory;


import com.effective.common.netty.cluster.command.api.*;
import com.effective.common.netty.cluster.command.domain.*;
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
     * 基础命令
     */
    BASE(AbstractCommand.COMMAND_NAME, AbstractCommand.class) {
        @Override
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(AbstractCommand.COMMAND_NAME);
        }
    },

    /**
     * 响应命令
     */
    RESPONSE_MESSAGE(Response.COMMAND_NAME, Response.class) {
        @Override
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(Response.COMMAND_NAME);
        }
    },

    /**
     * 发布消息命令
     */
    PUBLISH_MESSAGE(Publish.COMMAND_NAME, Publish.class) {
        @Override
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(Publish.COMMAND_NAME);
        }
    },

    /**
     * 心跳命令
     */
    PING_MESSAGE(Ping.COMMAND_NAME, Ping.class) {
        @Override
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(PingCommandHandler.class.getSimpleName());
        }
    },

    /**
     * 心跳命令
     */
    PONG_MESSAGE(Pong.COMMAND_NAME, Pong.class) {
        @Override
        public CommandHandler<Command> getHandler() {
            return handlerMap.get(PongCommandHandler.class.getSimpleName());
        }
    },

    /**
     * 断开连接命令
     */
    DISCONNECT_MESSAGE(Disconnect.COMMAND_NAME, Disconnect.class) {
        @Override
        public CommandHandler<Command> getHandler() {
            //return handlerMap.get(Disconnect.class.getSimpleName());
            return null;
        }
    };

    /**
     * commandName
     */
    private final String commandName;

    /**
     * class
     */
    private final Class<?> clazz;

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
    @SuppressWarnings("all")
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 命令执行容器
     */
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
    CommandFactory(String commandName, Class<?> clazz) {
        this.commandName = commandName;
        this.clazz = clazz;
    }

//    /**
//     * 注册命令执行器
//     *
//     * @param commandHandler 命令执行器
//     */
//    @SuppressWarnings("all")
//    public static void register(@NonNull CommandHandler<Command> commandHandler) {
//        commandHandler.getCommands().forEach(commandName -> {
//            if (!handlerMap.containsKey(commandName)) {
//                handlerMap.computeIfAbsent(commandName, s -> commandHandler);
//            } else {
//                log.warn("命令处理器已存在 CommandName:{} class:{}", commandName, commandHandler.getClass().getCanonicalName());
//            }
//        });
//    }


//    public static CommandHandler<Command> getHandler(@NonNull String commandName) {
//        if (handlerMap.isEmpty()) {
//            log.info("【服务端】命令工厂加载命令实现类");
//        }
//        return handlerMap.get(commandName);
//    }

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
