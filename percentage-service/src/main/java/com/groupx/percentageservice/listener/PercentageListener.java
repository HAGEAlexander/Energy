package com.groupx.percentageservice.listener;

import com.groupx.percentageservice.config.RabbitConfig;
import com.groupx.percentageservice.model.CurrentPercentage;
import com.groupx.percentageservice.model.HourlyAggregate;
import com.groupx.percentageservice.repository.CurrentPercentageRepository;
import com.groupx.percentageservice.repository.HourlyAggregateRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PercentageListener {

    private final HourlyAggregateRepository aggRepo;
    private final CurrentPercentageRepository pctRepo;

    public PercentageListener(HourlyAggregateRepository aggRepo,
                              CurrentPercentageRepository pctRepo) {
        this.aggRepo = aggRepo;
        this.pctRepo = pctRepo;
    }

    @RabbitListener(queues = RabbitConfig.UPDATES_QUEUE)
    public void handleUpdate(String hourStr) {
        LocalDateTime hour = LocalDateTime.parse(hourStr);
        HourlyAggregate agg = aggRepo.findById(hour)
                .orElseThrow(() -> new IllegalStateException("No aggregate for hour " + hour));

        double produced = agg.getCommunityProduced();
        double communityUsed = agg.getCommunityUsed();
        double communityDepleted = produced > 0
                ? Math.min(100.0, (communityUsed / produced) * 100.0)
                : 0.0;

        double gridPortion = (agg.getGridUsed() / (produced + agg.getGridUsed())) * 100.0;

        CurrentPercentage pct = new CurrentPercentage();
        pct.setHour(hour);
        pct.setCommunityDepleted(communityDepleted);
        pct.setGridPortion(gridPortion);

        pctRepo.save(pct);
    }
}
