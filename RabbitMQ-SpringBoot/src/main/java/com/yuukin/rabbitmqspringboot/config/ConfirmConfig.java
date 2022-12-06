package com.yuukin.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yuukin
 * @data 2022/12/5 14:20
 *
 * 配置类  发布确认高级
 */
@Configuration
public class ConfirmConfig {

    /**
     * 队列名称
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    /**
     * 交换机名称
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    /**
     * 路由
     */
    public static final String CONFIRM_ROUTING_KEY = "key1";





    /**
     * 备份交换机名称
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    /**
     * 备份队列名称
     */
    public static final String BACKUP_QUEUE_NAME = "backup_queue";

    /**
     * 报警队列名称
     */
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }

    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }


    @Bean
    public Binding queueBindingExchange(Queue confirmQueue, DirectExchange confirmExchange) {
        return BindingBuilder
                .bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }


    //备份交换机的创建
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明备份队列
    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //声明报警队列
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //绑定 备份队列绑定备份交换机
    @Bean
    public Binding backupQueueBindingBackupExchange(Queue backupQueue, FanoutExchange backupExchange){
        return BindingBuilder
                .bind(backupQueue)
                .to(backupExchange);
    }

    //绑定 报警队列绑定备份交换机
    @Bean
    public Binding warningQueueBindingBackupExchange(Queue warningQueue, FanoutExchange backupExchange){
        return BindingBuilder
                .bind(warningQueue)
                .to(backupExchange);
    }


}
