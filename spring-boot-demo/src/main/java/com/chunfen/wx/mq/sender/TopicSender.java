package com.chunfen.wx.mq.sender;

import com.chunfen.wx.config.RabbitConfig;
import com.chunfen.wx.constant.Json;
import com.chunfen.wx.domain.User;
import com.chunfen.wx.mq.receiver.HelloReceiver;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xi.w on 2019/5/2.
 */
//@Component
public class TopicSender {

    public static final Logger LOGGER = LoggerFactory.getLogger(HelloReceiver.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        User user = new User();
        int nextInt = RandomUtils.nextInt();
        user.setId(nextInt);
        user.setName("tom_" + nextInt);
        LOGGER.info("Sender : " + Json.toString(user));
        this.rabbitTemplate.convertAndSend(RabbitConfig.exchange, "topic.message", user);
    }
}
