package com.groupx.percentageservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // MUST match usage-service
    public static final String UPDATES_EXCHANGE    = "energy.updates.exchange";
    public static final String UPDATES_ROUTING_KEY = "energy.updated";
    public static final String UPDATES_QUEUE       = "energy.updates.queue";

    @Bean
    public DirectExchange updatesExchange() {
        return ExchangeBuilder
                .directExchange(UPDATES_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue updatesQueue() {
        return QueueBuilder
                .durable(UPDATES_QUEUE)
                .build();
    }

    @Bean
    public Binding updatesBinding(Queue updatesQueue, DirectExchange updatesExchange) {
        return BindingBuilder
                .bind(updatesQueue)
                .to(updatesExchange)
                .with(UPDATES_ROUTING_KEY);
    }
}
