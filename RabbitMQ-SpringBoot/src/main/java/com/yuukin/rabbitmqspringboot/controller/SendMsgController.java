package com.yuukin.rabbitmqspringboot.controller;

import com.yuukin.rabbitmqspringboot.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Yuukin
 * @data 2022/12/5 11:45
 *
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 开始发消息 已经在配置类中固定了延迟时间
     * @param message 消息
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间:{},发送一条信息给两个TTL队列:{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X","XA","消息来自TTL为10s的队列" + message);
        rabbitTemplate.convertAndSend("X","XB","消息来自TTL为40s的队列" + message);
    }

    /**
     * 开始发消息 消息TTL  在发消息时设置ttl
     * @param message 消息
     * @param ttlTime ttl时间
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsgWithTtl(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间:{},发送一条时长{}毫秒的信息给队列QC:{}",
                new Date().toString(), ttlTime ,message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            //发送消息时的延时时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    /**
     *基于插件的延时队列 发消息时设置延迟时间
     * @param message 消息
     * @param delayTime 延迟时间
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsgWithDelayTime(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info("当前时间:{},发送一条时长{}毫秒的信息给延迟队列delayed.queue:{}",
                new Date().toString(), delayTime ,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
                    //发送消息的时候 延迟时长 单位ms
                    msg.getMessageProperties().setDelay(delayTime);
                    return msg;
                });
    }


}
