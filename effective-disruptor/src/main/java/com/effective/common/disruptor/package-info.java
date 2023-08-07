package com.effective.common.disruptor;

/**
 * 注意事项
 * 1.RingBuffer.size 必须是2幂次方，否者会启动报错
 * 2.Start只能调用一次.再次调用会报错
 * 3.disruptor.shutdown 会终止Disruptor,终止前会阻塞的判断，
 *   3.1 是否有未消费的事件
 *   3.2 是否有消费者未消费完
 *   3.3 只有确定全部完成才会执行shutdown,shutdown 支持等待超时
 * 4.消费未完成就退出
 *
 */
