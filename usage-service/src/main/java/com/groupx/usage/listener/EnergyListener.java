package com.groupx.usage.listener;

import com.groupx.usage.config.RabbitConfig;
import com.groupx.usage.model.EnergyMessage;
import com.groupx.usage.model.HourlyAggregate;
import com.groupx.usage.repository.HourlyAggregateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class EnergyListener {
    private final HourlyAggregateRepository repo;
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper;

    public EnergyListener(HourlyAggregateRepository repo,
                          RabbitTemplate rabbit,
                          ObjectMapper mapper) {
        this.repo   = repo;
        this.rabbit = rabbit;
        this.mapper = mapper;  // assume JavaTimeModule already registered
    }

    @RabbitListener(queues = RabbitConfig.ENERGY_QUEUE)
    public void receive(String json) throws Exception {
        EnergyMessage msg = mapper.readValue(json, EnergyMessage.class);

        // Convert the incoming UTC timestamp to the local (Vienna) zone,
        // then truncate to the start of that hour:
        Instant instant = msg.getDatetime().toInstant();
        LocalDateTime hour = LocalDateTime
                .ofInstant(instant, ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.HOURS);

        HourlyAggregate agg = repo.findById(hour)
                .orElse(new HourlyAggregate(hour));

        if ("PRODUCER".equals(msg.getType())) {
            agg.setCommunityProduced(agg.getCommunityProduced() + msg.getKwh());
        } else {
            double used = agg.getCommunityUsed() + msg.getKwh();
            agg.setCommunityUsed(used);
            double excess = used - agg.getCommunityProduced();
            if (excess > 0) {
                agg.setGridUsed(agg.getGridUsed() + excess);
            }
        }

        repo.save(agg);

        // Notify the percentage service that this hour has new data:
        rabbit.convertAndSend(
                RabbitConfig.UPDATES_EXCHANGE,
                RabbitConfig.UPDATES_ROUTING_KEY,
                hour.toString()
        );
    }
}
