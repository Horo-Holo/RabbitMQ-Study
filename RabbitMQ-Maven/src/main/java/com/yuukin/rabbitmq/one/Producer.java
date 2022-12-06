package com.yuukin.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/2 16:00
 * 生产者
 * 发消息
 */
public class Producer {
    //队列名
    private static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂连接rabbitmq队列
        factory.setHost("192.168.88.100");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列中消息是否持久化，默认消息存储在内存中，持久化存储在磁盘中
         * 3.该队列是否只供一个消费者进行消费 是否进行消息共享  true表示允许一个消费者使用  默认不允许
         * 4.是否自动删除  当所有消费者都与这个队列断开连接时，这个队列会自动删除 true自动删除
         * 5.其他参数
         */
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);
        //发消息
        for (int i = 0; i < 10; i++) {
            String message = "Hello World!" + i;
            if (i == 5) {
                AMQP.BasicProperties properties =
                        new AMQP.BasicProperties()
                                .builder()
                                .priority(5)
                                .build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
        /**
         * 发送一个消息
         * 1.表示发送到哪一个交换机
         * 2.路由的key值 本次是队列名
         * 3.其他参数信息
         * 4.发送的消息的消息体
         */

        System.out.println("消息发送完毕");



    }
}
