package com.effective.common.netty.cluster.command.domain;

import com.effective.common.netty.cluster.command.api.AbstractCommand;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(chain = true)
public class Response extends AbstractCommand<Integer> {

    /**
     * 命令名称
     */
    public static String COMMAND_NAME = "responseCommand";

    /**
     * 消息id
     */
    private int messageId;

    /**
     * 机器id
     */
    private String brokerId;

    public Response() {
        super(COMMAND_NAME);
    }

    /**
     * 获取消息ID
     * @return messageId
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * 设置消息ID
     * @param messageId 消息ID
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取机器ID
     * @return brokerId
     */
    public String getBrokerId() {
        return brokerId;
    }

    /**
     * 设置机器ID
     * @param brokerId 机器ID
     */
    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }
}
