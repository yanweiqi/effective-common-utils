package com.effective.common.netty.cluster.command;


import java.util.List;

/**
 * 命令执行器
 * @param <T>
 */
public interface CommandHandler<T extends Command> {

    /**
     * 持异步执行
     *
     * @return ture/false
     */
    boolean asyncRunning();

    /**
     * 支持命令类型
     *
     * @param command 命令
     * @return true/false
     */
    boolean supportsCommandType(Class<? extends Command> command);

    /**
     * 应用命令
     *
     * @return true/false
     */
    List<String> getApplyCommands();

    /**
     * 保存消息体
     *
     * @param messageBody 消息体
     * @return ture/false
     */
    T restore(String messageBody);


    /**
     * 执行命令
     *
     * @param command 命令
     * @throws Exception 异常
     */
    void execute(T command) throws Exception;

}
