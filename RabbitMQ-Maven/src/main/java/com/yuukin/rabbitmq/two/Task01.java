package com.yuukin.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/3 13:17
 */
public class Task01 {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();


        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null,next.getBytes());
            System.out.println("发送消息完成  " + next);
        }
    }
}
