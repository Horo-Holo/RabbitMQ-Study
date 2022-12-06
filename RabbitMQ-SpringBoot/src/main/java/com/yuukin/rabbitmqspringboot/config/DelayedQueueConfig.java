package com.yuukin.rabbitmqspringboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author Yuukin
 * @data 2022/12/5 13:12
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 队列名称
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    /**
     * 交换机名称
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    /**
     * 路由
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    /**
     * 声明队列
     * @return 队列
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     *声明交换机 需要自定义交换机
     * @return 延迟交换机
     */
    @Bean
    public CustomExchange delayedExchange() {

        HashMap<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "direct");
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他的参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message",
                true, false, arguments);
    }


    /**
     * 绑定交换机与队列
     * @param delayedQueue 延迟队列
     * @param delayedExchange  延迟交换机
     * @return  绑定
     */
    @Bean
    public Binding delayedQueueBindingDelayedExchange(Queue delayedQueue, CustomExchange delayedExchange) {

        return BindingBuilder
                .bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();

    }
}
