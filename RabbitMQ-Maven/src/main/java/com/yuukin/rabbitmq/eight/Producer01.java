package com.yuukin.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/4 14:10
 * 死信队列 生产者
 */
public class Producer01 {

    /**
     * 普通交换机
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
//        AMQP.BasicProperties properties =
//                new AMQP.BasicProperties()
//                        .builder().expiration("10000").build();

        //延迟消息 设置TTL
        for (int i = 0; i < 10; i++) {
            String message = "info" + i ;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan", null, message.getBytes());
        }

    }

}
