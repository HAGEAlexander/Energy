package com.groupx.usage.service;

import com.groupx.usage.config.RabbitConfig;
import com.groupx.usage.model.EnergyMessage;
import com.groupx.usage.model.HourlyAggregate;
import com.groupx.usage.repository.HourlyAggregateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UsageService {
    private static final Logger log = LoggerFactory.getLogger(UsageService.class);

    private final HourlyAggregateRepository aggRepo;
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper;

    public UsageService(HourlyAggregateRepository aggRepo,
                        RabbitTemplate rabbit,
                        ObjectMapper mapper) {
        this.aggRepo = aggRepo;
        this.rabbit  = rabbit;
        // ensure JavaTime support and stop Jackson shifting offsets into JVM TZ
        this.mapper = mapper
                .registerModule(new JavaTimeModule())
                .disable(com.fasterxml.jackson.databind.DeserializationFeature
                        .ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    }

    /** Called via CommandLineRunner; no-op */
    public void startProcessing() { }

    /**
     * Consume each JSON payload, update hourly_aggregates,
     * then publish an update event for the percentage‐service.
     */
    @Transactional
    public void processMessage(String json) {
        try {
            EnergyMessage msg = mapper.readValue(json, EnergyMessage.class);

            // OffsetDateTime → LocalDateTime → truncate to the hour
            LocalDateTime hour = msg.getDatetime()
                    .toLocalDateTime()
                    .truncatedTo(ChronoUnit.HOURS);

            HourlyAggregate agg = aggRepo.findById(hour)
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

            aggRepo.save(agg);

            // fire off an update so the percentage‐service recalculates
            rabbit.convertAndSend(
                    RabbitConfig.UPDATES_EXCHANGE,
                    RabbitConfig.UPDATES_ROUTING_KEY,
                    hour.toString()
            );

            log.debug("Processed {} for hour {}, agg={}", msg.getType(), hour, agg);
        } catch (Exception e) {
            log.error("Failed to process message: {}", json, e);
        }
    }
}
