package org.example.messageservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE = "message-published";
    public static final String EXCHANGE = "chat.exchange";
    public static final String ROUTING_KEY = "message.published";

    @Bean
    public Queue messagePublishedQueue() {
        return new Queue(QUEUE, true); //true = durable, överlever omstart av RabbitMQ
    }

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue messagePublishedQueue, TopicExchange chatExchange) {
        return BindingBuilder.bind(messagePublishedQueue).to(chatExchange).with(ROUTING_KEY);
    }

    @Bean
    //JacksonJsonMessageConverter ersätter Jackson2JsonMessageConverter sedan 4.0 för att undvika beroende av Jackson 2.x
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
