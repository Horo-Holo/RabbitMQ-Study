package com.yuukin.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.yuukin.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuukin
 * @data 2022/12/3 16:38
 * 发布确认模式
 * 使用时间 比较那种确认方式是好的
 * 1.单个确认模式
 * 2.批量确认模式
 * 3.异步批量确认
 */
public class ConfirmMessage {

    /**
     * 批量发消息的个数
     */
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //单个确认 发布1000个单独确认消息，耗时 399ms
        //ConfirmMessage.publishMessageSingle();
        //批量确认  发布1000个确认消息，耗时 72ms
        //ConfirmMessage.publishMessageBatch();
        //异步批量确认 发布1000个异步确认消息，耗时 31ms
        ConfirmMessage.publishMessageAsync();
    }

    /**
     * 单个确认
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessageSingle() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if(flag) {
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时 " + (end - begin) + "ms");

    }

    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息的大小
        int batchSize = 100;

        //批量发送消息 批量发布确认
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null ,message.getBytes());
            if (i % batchSize == 0) {
                //发布确认
                channel.confirmSelect();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时 " + (end - begin) + "ms");
    }

    public static void publishMessageAsync() throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表 适用于高并发情况下
         * 1. 将序号与消息关联
         * 2. 批量删除条目
         * 3. 支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                //2.删除已经确认的消息 剩下的就是为确认的消息
                ConcurrentNavigableMap<Long, String> confirmd = outstandingConfirms.headMap(deliveryTag, true);
                confirmd.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息" + deliveryTag);

        };
        /**
         *  消息确认失败回调函数
         *  1.消息的标识
         *  2.是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3.打印一下未确认的消息有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息" + message +  "  未确认的消息的标记 " + deliveryTag);
        };
        //准备消息监听器 监听哪些消息成功了 那些消息失败了
        //异步监听
        channel.addConfirmListener(ackCallback, nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //1.此处记录所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo() - 1, message);

        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时 " + (end - begin) + "ms");
    }

}
