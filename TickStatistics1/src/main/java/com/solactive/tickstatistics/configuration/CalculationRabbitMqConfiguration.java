package com.solactive.tickstatistics.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalculationRabbitMqConfiguration {

    @Value("${rabbitmq.calculation.queue.name}")
    private String queueName;

    @Value("${rabbitmq.calculation.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.calculation.routing.name}")
    private String routingName;

    @Bean
    public Queue calculationQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange calculationDirectExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding calculationBinding(final Queue calculationQueue, final DirectExchange calculationDirectExchange){
        return BindingBuilder.bind(calculationQueue).to(calculationDirectExchange).with(routingName);
    }
}
