package com.example.rest_service.controller;

import com.example.rest_service.energy.CurrentPercentage;
import com.example.rest_service.energy.Usage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/energy")
public class EnergyController {

    @GetMapping("/current")
    public CurrentPercentage getCurrentPercentage() {
        // Dummy-Daten als Beispiel
        LocalDateTime currentHour = LocalDateTime.of(2025, 1, 10, 14, 0, 0);
        double communityDepleted = 100.0;
        double gridPortion = 5.63;
        return new CurrentPercentage(currentHour, communityDepleted, gridPortion);
    }

    /**
     * GET /energy/historical?start=2025-01-10T14:00:00&end=2025-01-10T16:00:00
     * Liefert Test Daten für den angefragten Zeitraum.
     */
    @GetMapping("/historical")
    public List<Usage> getHistoricalUsage(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        // Test Daten zurückgegeben.
        List<Usage> usageList = new ArrayList<>();

        usageList.add(new Usage(LocalDateTime.of(2025, 1, 10, 14, 0), 18.05, 18.05, 1.076));
        usageList.add(new Usage(LocalDateTime.of(2025, 1, 10, 15, 0), 15.015, 14.033, 2.049));

        return usageList;
    }
}