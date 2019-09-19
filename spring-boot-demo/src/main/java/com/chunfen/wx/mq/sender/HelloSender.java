package com.chunfen.wx.mq.sender;

import com.chunfen.wx.mq.receiver.HelloReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by xi.w on 2019/5/1.
 */
//@Component
public class HelloSender {

    public static final Logger LOGGER = LoggerFactory.getLogger(HelloReceiver.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello " + new Date();
        LOGGER.info("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

}