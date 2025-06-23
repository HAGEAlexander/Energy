package com.groupx.usage.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String ENERGY_QUEUE        = "energy";
    public static final String UPDATES_EXCHANGE    = "energy.updates.exchange";
    public static final String UPDATES_ROUTING_KEY = "energy.updated";
    public static final String UPDATES_QUEUE       = "energy.updates.queue";

    @Bean
    public Queue energyQueue() {
        return new Queue(ENERGY_QUEUE, true);
    }

    @Bean
    public DirectExchange updatesExchange() {
        return new DirectExchange(UPDATES_EXCHANGE, true, false);
    }

    @Bean
    public Queue updatesQueue() {
        return new Queue(UPDATES_QUEUE, true);
    }

    @Bean
    public Binding bindUpdates() {
        return BindingBuilder
                .bind(updatesQueue())
                .to(updatesExchange())
                .with(UPDATES_ROUTING_KEY);
    }
}
