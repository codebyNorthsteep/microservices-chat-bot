package org.example.botservice.config;

import org.springframework.amqp.core.*;
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
        return new Queue(QUEUE, true);
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
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
