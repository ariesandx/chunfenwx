package com.chunfen.wx.config;

import net.sf.jsqlparser.statement.select.Top;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xi.w on 2019/5/1.
 */
//@Configuration
public class RabbitConfig {

    public final static String message = "topic1";
    public final static String messages = "topic2";

    public final static String exchange = "topicExchange";

    @Bean
    public Queue Queue() {
        return new Queue("hello");
    }

    @Bean
    public Queue topicMessageQueue(){
        return new Queue(message);
    }

    @Bean
    public Queue topicMessagesQueue(){
        return new Queue(messages);
    }

    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    Binding bindingTopicMessage(TopicExchange topicExchange, Queue topicMessageQueue){
        return BindingBuilder.bind(topicMessageQueue).to(topicExchange).with("topic.message");
    }

    @Bean
    Binding bindingTopicMessages(TopicExchange topicExchange, Queue topicMessagesQueue){
        return BindingBuilder.bind(topicMessagesQueue).to(topicExchange).with("topic.#");
    }
}
