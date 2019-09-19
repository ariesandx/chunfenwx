package com.chunfen.wx.mq.receiver;

import com.chunfen.wx.constant.Json;
import com.chunfen.wx.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by xi.w on 2019/5/2.
 */
//@Component
//@RabbitListener(queues = "topic1")
//@RabbitListener(queues = "topic2")
public class TopicReceiver {

    public static final Logger LOGGER = LoggerFactory.getLogger(HelloReceiver.class);

    @RabbitHandler
    public void handler(User user){
        LOGGER.info("Receiver : " + Json.toString(user));
    }
}
