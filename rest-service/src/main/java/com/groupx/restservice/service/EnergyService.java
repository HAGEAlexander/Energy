package com.groupx.restservice.service;

import com.groupx.restservice.model.CurrentPercentage;
import com.groupx.restservice.model.HourlyAggregate;
import com.groupx.restservice.repository.CurrentPercentageRepository;
import com.groupx.restservice.repository.HourlyAggregateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EnergyService {

    private final CurrentPercentageRepository pctRepo;
    private final HourlyAggregateRepository aggRepo;

    public EnergyService(CurrentPercentageRepository pctRepo,
                         HourlyAggregateRepository aggRepo) {
        this.pctRepo = pctRepo;
        this.aggRepo = aggRepo;
    }

    public CurrentPercentage getCurrentPercentage() {
        return pctRepo.findTopByOrderByHourDesc()
                .orElseGet(() -> {
                    // whatever makes sense: zero‐percent at “now”
                    CurrentPercentage cp = new CurrentPercentage();
                    cp.setHour(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS));
                    cp.setCommunityDepleted(0.0);
                    cp.setGridPortion(0.0);
                    return cp;
                });
    }

    public List<HourlyAggregate> getHistorical(LocalDateTime start, LocalDateTime end) {
        return aggRepo.findByHourBetween(start, end);
    }
}
