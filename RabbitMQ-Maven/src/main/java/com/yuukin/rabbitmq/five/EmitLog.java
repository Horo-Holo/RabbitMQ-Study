package com.yuukin.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/4 10:51
 * 发消息给交换机
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null ,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息 ：" + message);
        }
    }
}
