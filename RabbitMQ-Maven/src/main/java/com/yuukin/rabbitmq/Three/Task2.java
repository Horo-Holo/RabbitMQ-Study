package com.yuukin.rabbitmq.Three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/3 15:02
 * 消息在手动应答时是不消失的
 *
 */
public class Task2 {
    private static final String TASK_QUEUE_ACK = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();

        //队列持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_ACK, durable,false, false, null);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String message = scanner.next();
            //设置生产者发送消息为持久化消息(要求保存在磁盘上)
            channel.basicPublish("", TASK_QUEUE_ACK, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：  " + message);
        }
    }

}
