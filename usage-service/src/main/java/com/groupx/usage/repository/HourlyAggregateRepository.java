package com.groupx.usage.repository;

import com.groupx.usage.model.HourlyAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface HourlyAggregateRepository
        extends JpaRepository<HourlyAggregate, LocalDateTime> {
}
