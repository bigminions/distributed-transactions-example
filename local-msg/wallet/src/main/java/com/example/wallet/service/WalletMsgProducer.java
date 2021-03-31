package com.example.wallet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class WalletMsgProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Async
    public void sendToRabbitMQAsync(String routingKey, Object msg) {
        if (log.isDebugEnabled())
            log.debug("start send msg to rocket " + msg.toString());

        rabbitTemplate.convertAndSend(routingKey, msg);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (log.isDebugEnabled())
            log.debug("from mq confirm {}", correlationData);

    }
}
