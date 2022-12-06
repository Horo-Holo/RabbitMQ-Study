package com.yuukin.rabbitmqspringboot.consumer;

import com.rabbitmq.client.Channel;
import com.yuukin.rabbitmqspringboot.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Yuukin
 * @data 2022/12/5 13:35
 * 基于插件的延时队列的 消费者
 */
@Component
@Slf4j
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间:{} , 收到延迟队列的消息: {} " , new Date().toString(), msg);
    }
}
