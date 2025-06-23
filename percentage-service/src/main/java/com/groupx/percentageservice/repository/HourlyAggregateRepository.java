package com.groupx.percentageservice.repository;


import com.groupx.percentageservice.model.HourlyAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface HourlyAggregateRepository
        extends JpaRepository<HourlyAggregate, LocalDateTime> {
}
