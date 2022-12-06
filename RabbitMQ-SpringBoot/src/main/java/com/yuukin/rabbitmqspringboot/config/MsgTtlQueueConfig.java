package com.yuukin.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author Yuukin
 * @data 2022/12/5 12:09
 */
@Configuration
public class MsgTtlQueueConfig {
    /**
     * 普通队列的名称
     */
    public static final String QUEUE_C = "QC";

    /**
     * 死信交换机的名称
     */
    public static final String Y_DEAD_LETTER_EXCHANGE="Y";

    @Bean
    public Queue queueC() {
        return QueueBuilder
                .durable(QUEUE_C)
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey("YD")
                .build();
    }

    @Bean
    public Binding queueCBindingX(Queue queueC, DirectExchange xExchange) {
        return BindingBuilder
                .bind(queueC)
                .to(xExchange)
                .with("XC");
    }
}
