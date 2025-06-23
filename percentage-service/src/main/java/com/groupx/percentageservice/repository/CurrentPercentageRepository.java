package com.groupx.percentageservice.repository;

import com.groupx.percentageservice.model.CurrentPercentage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CurrentPercentageRepository
        extends JpaRepository<CurrentPercentage, LocalDateTime> {
}
