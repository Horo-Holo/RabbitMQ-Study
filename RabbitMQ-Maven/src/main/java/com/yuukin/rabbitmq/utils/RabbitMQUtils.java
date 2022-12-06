package com.yuukin.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/3 12:51
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
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
        return channel;
    }
}
