package com.chunfen.wx.mq.receiver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by xi.w on 2019/5/1.
 */
//@Component
//@RabbitListener(queues = "hello")
public class HelloReceiver {

    public static final Logger LOGGER = LoggerFactory.getLogger(HelloReceiver.class);

    @RabbitHandler
    public void process(String hello) {
        LOGGER.info("Receiver : " + hello);
    }
}
