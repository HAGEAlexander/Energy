package com.groupx.restservice.repository;

import com.groupx.restservice.model.HourlyAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface HourlyAggregateRepository
        extends JpaRepository<HourlyAggregate, LocalDateTime> {
    List<HourlyAggregate> findByHourBetween(LocalDateTime start, LocalDateTime end);
}
