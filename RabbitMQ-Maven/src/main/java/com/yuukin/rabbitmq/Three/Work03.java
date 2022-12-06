package com.yuukin.rabbitmq.Three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;
import com.yuukin.rabbitmq.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/3 15:07
 */
public class Work03 {
    private static final String TASK_QUEUE_ACK = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2等待接受消息处理时间较长");

        //手动接收消息
        boolean autoAck = false;
        DeliverCallback deliverCallback = (consumerTag, message) -> {

            SleepUtils.sleep(30);
            System.out.println("接受到的消息  " + new String(message.getBody(), "UTF-8" ));
            /**
             * 1.消息的标记
             * 2.是否批量应答 false表示不批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者取消消费接口回调");
        };

        //设置不公平分发
        //int prefetchCount = 1;
        //预取值为5
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        channel.basicConsume(TASK_QUEUE_ACK, autoAck, deliverCallback, cancelCallback);
    }
}
