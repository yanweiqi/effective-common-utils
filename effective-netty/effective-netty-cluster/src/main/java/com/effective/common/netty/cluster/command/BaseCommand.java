package com.effective.common.netty.cluster.command;

import com.effective.common.netty.cluster.feeback.Feedback;
import com.effective.common.netty.cluster.handler.CommandHeader;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Command
 *
 * @date 11/2/2020
 */
@Slf4j
@Data
@Accessors(chain = true)
public class BaseCommand<T> implements Command, Serializable, Cloneable {

    public static String COMMAND_NAME = "base";

    private transient CommandHeader header = new CommandHeader();

    private transient Feedback feedback;

    private transient ChannelHandlerContext channelHandlerContext;

    @Override
    public Feedback getFeedback() {
        return feedback;
    }

    @Override
    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    @Override
    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    private T commandBody;

    public BaseCommand(String commandName) {
        this.header.setCommandName(commandName);
    }

    @Override
    public String getCommandName() {
        return header.getCommandName();
    }

    @Override
    public BaseCommand<T> clone() throws CloneNotSupportedException {
        BaseCommand baseCommand = (BaseCommand<T>) super.clone();
        baseCommand.header = header.clone();
        return baseCommand;
    }
}
