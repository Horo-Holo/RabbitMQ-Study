package com.yuukin.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author Yuukin
 * @data 2022/12/5 11:24
 * TTL队列 配置文件类
 */
@Configuration
public class TtlQueueConfig {
    /**
     * 普通交换机名称
     */
    public static final String X_EXCHANGE="X";
    /**
     * 死信交换机名称
     */
    public static final String Y_DEAD_LETTER_EXCHANGE="Y";
    /**
     * 普通队列名称
     */
    public static final String QUEUE_A="QA";
    public static final String QUEUE_B="QB";
    /**
     * 死信队列名称
     */
    public static final String DEAD_LETTER_QUEUE="QD";

    /**
     * 声明交换机
     * @return 直接交换机
     */
    @Bean
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明交换机
     * @return 直接交换机
     */
    @Bean
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明普通队列 过期时间10s
     * @return 普通队列
     */
    @Bean
    public Queue queueA() {
        return QueueBuilder
                .durable(QUEUE_A)
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey("YD")
                .ttl(10 * 1000)
                .build();
    }

    /**
     * 声明普通队列 过期时间40s
     * @return 普通队列
     */
    @Bean
    public Queue queueB() {
        return QueueBuilder
                .durable(QUEUE_B)
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey("YD")
                .ttl(40 * 1000)
                .build();
    }

    /**
     * 声明死信队列
     * @return 死信队列
     */
    @Bean
    public Queue queueD() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    /**
     * 绑定队列与交换机
     * @param queueA 队列A
     * @param xExchange 交换机X
     * @return 绑定
     */
    @Bean
    public Binding queueABindingX(Queue queueA, DirectExchange xExchange) {
        return BindingBuilder
                .bind(queueA)
                .to(xExchange)
                .with("XA");
    }

    /**
     * 绑定队列与交换机
     * @param queueB 队列B
     * @param xExchange 交换机X
     * @return 绑定
     */
    @Bean
    public Binding queueBBindingX(Queue queueB, DirectExchange xExchange) {
        return BindingBuilder
                .bind(queueB)
                .to(xExchange)
                .with("XB");
    }

    /**
     * 绑定队列与交换机
     * @param queueD 队列D
     * @param yExchange 交换机Y
     * @return 绑定
     */
    @Bean
    public Binding queueDBindingY(Queue queueD, DirectExchange yExchange) {
        return BindingBuilder
                .bind(queueD)
                .to(yExchange)
                .with("YD");
    }

}
