package com.yuukin.rabbitmqspringboot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Yuukin
 * @data 2022/12/5 11:52
 *
 * 队列TTL消费者
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间为:{},收到死信队列的消息:{}", new Date().toString(), msg);
    }
}
