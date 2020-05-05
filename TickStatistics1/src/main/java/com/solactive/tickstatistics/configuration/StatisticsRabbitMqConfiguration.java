package com.solactive.tickstatistics.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticsRabbitMqConfiguration {

    @Value("${rabbitmq.statistics.queue.name}")
    private String queueName;

    @Value("${rabbitmq.statistics.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.statistics.routing.name}")
    private String routingName;

    @Bean
    public Queue statisticsQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange statisticsDirectExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding statisticsBinding(final Queue statisticsQueue, final DirectExchange statisticsDirectExchange){
        return BindingBuilder.bind(statisticsQueue).to(statisticsDirectExchange).with(routingName);
    }
}
