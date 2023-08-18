package com.effective.common.netty.cluster.command.api;

import com.effective.common.netty.cluster.command.domain.HeaderCommand;
import com.effective.common.netty.cluster.feeback.Feedback;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;


@Slf4j
@Data
@Accessors(chain = true)
public class AbstractCommand<T> implements Command, Serializable, Cloneable {

    public static String COMMAND_NAME = "base";

    private transient HeaderCommand header = new HeaderCommand();

    private transient Feedback feedback;

    private transient ChannelHandlerContext channelHandlerContext;

    private T commandBody;

    public AbstractCommand(String commandName) {
        this.header.setCommandName(commandName);
    }

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

    @Override
    public String getCommandName() {
        return header.getCommandName();
    }

    @Override
    public AbstractCommand<T> clone() throws CloneNotSupportedException {
        AbstractCommand abstractCommand = (AbstractCommand<T>) super.clone();
        abstractCommand.header = header.clone();
        return abstractCommand;
    }
}
