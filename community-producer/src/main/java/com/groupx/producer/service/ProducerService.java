package com.groupx.producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.Random;

@Service
public class ProducerService {
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random rnd = new Random();

    public ProducerService(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void startProducing() throws Exception {
        while (true) {
            double factor = 0.5 + 0.5 * rnd.nextDouble();
            double kwh = 0.002 + 0.005 * factor * rnd.nextDouble();
            Map<String,Object> msg = Map.of(
                    "type","PRODUCER",
                    "association","COMMUNITY",
                    "kwh", Math.round(kwh*10000)/10000.0,
                    "datetime", Instant.now().toString()
            );
            rabbit.convertAndSend("energy", mapper.writeValueAsString(msg));
            Thread.sleep(1000 + rnd.nextInt(4000));
        }
    }
}