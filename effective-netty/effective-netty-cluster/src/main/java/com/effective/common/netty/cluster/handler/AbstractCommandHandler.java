package com.effective.common.netty.cluster.handler;

import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.utils.JSONUtil;
import com.effective.common.netty.cluster.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class AbstractCommandHandler<T extends Command> implements CommandHandler<T>, ExecutorsProvider {

    /**
     * 是否异步执行
     *
     * @return true/false
     */
    @Override
    public boolean asyncRunning() {
        return true;
    }


    /**
     * 命令执行逻辑
     *
     * @param command 命令
     * @throws Exception 异常
     */
    @Override
    public void execute(T command) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(), command, x -> log.info("【服务端】CommandHandler执行命令 {},Command:{}", command.getCommandName(), command));
        if (getCommands().contains(command.getCommandName())) {
            try {
                handle(command);
                if (Objects.nonNull(command.getFeedback())) {
                    if (Objects.nonNull(command.getChannelHandlerContext())) {
                        command.getFeedback().setChannelHandlerContext(command.getChannelHandlerContext());
                    }
                    command.getFeedback().acknowledge(null);
                }
            } catch (Exception e) {
                log.error("[CommandHandler]Error in processing command! message={}", e.getMessage(), e);
                if (Objects.nonNull(command.getFeedback())) {
                    if (Objects.nonNull(command.getChannelHandlerContext())) {
                        command.getFeedback().setChannelHandlerContext(command.getChannelHandlerContext());
                    }
                    command.getFeedback().negativeAcknowledge(null);
                }
            }
        } else {
            log.error("[CommandHandler]This handler can't handler this command, commandName={}, listener class={}", command.getCommandName(), this.getClass().getCanonicalName());
        }
    }

    /**
     * 处理逻辑
     *
     * @param command 命令
     */
    public abstract void handle(T command);

    /**
     * 存储消息体
     *
     * @param messageBody 消息体
     * @return T 泛型对象
     */
    @Override
    public T restore(String messageBody) {
        return JSONUtil.fromJson(messageBody, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    /**
     * Get executor service pool
     *
     * @return {@link ExecutorService}
     */
    @Override
    public ExecutorService getExecutorService() {
//        ExecutorService executorService = commandContext.getExecutorServiceMap().get(getApplyCommands().get(0));
//        if (Objects.isNull(executorService)) {
//            executorService = commandContext.getExecutorServiceMap().get(CommandContext.DEFAULT_POOL);
//        }
//        return executorService;
        return null;
    }

}
