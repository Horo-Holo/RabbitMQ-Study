package com.yuukin.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/3 13:04
 * 这是一个工作线程
 */
public class Work01 {

    public static final String QUEUE_NAME = "hello";

    //接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //消息的接受

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受到的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑");
        };

        System.out.println("C2等待接受消息。。。。。。");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
