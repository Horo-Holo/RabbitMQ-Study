package com.yuukin.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/2 16:23
 */
public class Consumer {
    private static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂连接rabbitmq队列
        factory.setHost("192.168.88.100");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息消费被中断");
        };
        /**
         * 消费者消费消息
         * 1.消费那个队列
         * 2.消费成功之后是否要自动应答  true 代表自动应答 false代表手动应答
         * 3.当一个消息发送过来后的回调接口
         * 4.当一个消费者取消订阅时的回调接口;取消消费者订阅队列时除了使用{@link Channel#basicCancel}之外的所有方式都会调用该回调方法
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback, cancelCallback);
    }
}
