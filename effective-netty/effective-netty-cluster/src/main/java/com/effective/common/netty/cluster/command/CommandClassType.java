package com.effective.common.netty.cluster.command;


public enum CommandClassType {

    /**
     * 基础命令
     */
    BASE(BaseCommand.COMMAND_NAME, BaseCommand.class),

    /**
     * 响应命令
     */
    RESPONSE_MESSAGE(ResponseCommand.COMMAND_NAME, ResponseCommand.class),

    /**
     * 发布消息命令
     */
    PUBLISH_MESSAGE(PublishMessageCommand.COMMAND_NAME, PublishMessageCommand.class),

    /**
     * 断开连接命令
     */
    DISCONNECT_MESSAGE(DisconnectCommand.COMMAND_NAME, DisconnectCommand.class);

    /**
     * name
     */
    private String name;

    /**
     * class
     */
    private Class clazz;

    CommandClassType(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }

    public static CommandClassType nameOf(String name) {
        for (CommandClassType type : CommandClassType.values()) {
            if (name.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("type do not supported");
    }

}
