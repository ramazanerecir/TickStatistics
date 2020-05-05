package com.solactive.tickstatistics.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickRabbitMqConfiguration {

    @Value("${rabbitmq.tick.queue.name}")
    private String queueName;

    @Value("${rabbitmq.tick.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.tick.routing.name}")
    private String routingName;

    @Bean
    public Queue tickQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange tickDirectExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding tickBinding(final Queue tickQueue, final DirectExchange tickDirectExchange){
        return BindingBuilder.bind(tickQueue).to(tickDirectExchange).with(routingName);
    }
}
