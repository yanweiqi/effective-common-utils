package com.effective.common.akka.part1.msg;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-1 4:26 PM
 */
@Data
public final class MessageBean {

    private final int visitCount;

    private final List<String> userName;

    private final Map<String,String> citys;

}

