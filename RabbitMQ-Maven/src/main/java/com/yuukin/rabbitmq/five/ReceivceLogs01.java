package com.yuukin.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/4 10:36
 * 负责消息的接收
 */
public class ReceivceLogs01 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        /**
         * 声明一个临时队列 当消费者断与队列的连接时 队列就自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上。。。。。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceivceLogs01控制台打印接收到的消息 " + new String(message.getBody(), StandardCharsets.UTF_8));
        };



        channel.basicConsume(queue,true,deliverCallback, cancelCallback->{});

    }

}
