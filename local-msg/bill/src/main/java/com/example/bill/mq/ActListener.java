package com.example.bill.mq;

import com.example.bill.cfg.RabbitMqCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActListener {

    @RabbitListener(queues = RabbitMqCfg.QueueName.ACT_QUEUE)
    public void processAct(String data) {
        if (log.isDebugEnabled())
            log.debug("received data from mq: act_queue {}", data);
        System.out.println(data);
    }
}
