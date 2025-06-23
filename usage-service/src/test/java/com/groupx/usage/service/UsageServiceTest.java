package com.groupx.usage.service;

import com.groupx.usage.config.RabbitConfig;
import com.groupx.usage.model.HourlyAggregate;
import com.groupx.usage.repository.HourlyAggregateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UsageServiceTest {
    private HourlyAggregateRepository repo;
    private RabbitTemplate            rabbit;
    private UsageService              service;
    private ObjectMapper              mapper;

    @BeforeEach
    void setUp() {
        repo    = mock(HourlyAggregateRepository.class);
        rabbit  = mock(RabbitTemplate.class);
        mapper  = new ObjectMapper().registerModule(new JavaTimeModule());
        service = new UsageService(repo, rabbit, mapper);
    }

    @Test
    void whenProducerMessage_thenCommunityProducedIncremented() throws Exception {
        // 1) pick a fixed OffsetDateTime at +02:00:
        OffsetDateTime odt = OffsetDateTime.of(
                2025, 6, 23, 17, 45, 12, 0,
                ZoneOffset.ofHours(2)
        );
        String dt = odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // 2) embed that into the JSON payload:
        String json = String.format(
                "{\"type\":\"PRODUCER\",\"association\":\"COMMUNITY\",\"kwh\":1.5,\"datetime\":\"%s\"}",
                dt
        );

        // No existing aggregate → repo.findById(...) returns empty:
        when(repo.findById(any())).thenReturn(Optional.empty());

        // process it:
        service.processMessage(json);

        // --- verify the saved aggregate ---
        ArgumentCaptor<HourlyAggregate> aggCap = ArgumentCaptor.forClass(HourlyAggregate.class);
        verify(repo).save(aggCap.capture());
        HourlyAggregate saved = aggCap.getValue();

        // should have truncated to 17:00 LOCAL (dropping minutes/seconds, offset irrelevant):
        assertEquals(
                LocalDateTime.of(2025, 6, 23, 17, 0),
                saved.getHour()
        );

        // --- verify the Rabbit send uses the same truncated hour string ---
        ArgumentCaptor<String> hourCap = ArgumentCaptor.forClass(String.class);
        verify(rabbit).convertAndSend(
                eq(RabbitConfig.UPDATES_EXCHANGE),
                eq(RabbitConfig.UPDATES_ROUTING_KEY),
                hourCap.capture()
        );

        // LocalDateTime.toString() gives "2025-06-23T17:00"
        assertEquals("2025-06-23T17:00", hourCap.getValue());
    }

    @Test
    void whenUserExceedsProduction_thenGridUsedIncrements() throws Exception {
        // for user tests we can still use any OffsetDateTime.now()
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.ofHours(2));
        String dt = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String json = String.format(
                "{\"type\":\"USER\",\"association\":\"COMMUNITY\",\"kwh\":2.0,\"datetime\":\"%s\"}",
                dt
        );

        // set up an existing hour‐aggregate with 1.0 kWh produced
        var existing = new HourlyAggregate(
                now.toLocalDateTime().truncatedTo(ChronoUnit.HOURS)
        );
        existing.setCommunityProduced(1.0);
        when(repo.findById(any())).thenReturn(Optional.of(existing));

        service.processMessage(json);

        // verify gridUsed increased by the excess (2.0 − 1.0 == 1.0)
        verify(repo).save(argThat(agg ->
                agg.getCommunityProduced() == 1.0 &&
                        agg.getCommunityUsed()     == 2.0 &&
                        agg.getGridUsed()          == 1.0
        ));
    }
}
