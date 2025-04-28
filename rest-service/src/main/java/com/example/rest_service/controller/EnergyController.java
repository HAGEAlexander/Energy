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

        LocalDateTime currentHour = LocalDateTime.of(2025, 1, 9, 9, 0, 0);
        double communityDepleted = 99.0;
        double gridPortion = 9.99;
        return new CurrentPercentage(currentHour, communityDepleted, gridPortion);
    }
    @GetMapping("/historical")
    public List<Usage> getHistoricalUsage(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {


        List<Usage> usageList = new ArrayList<>();

        usageList.add(new Usage(LocalDateTime.of(2025, 1, 9, 9, 0), 22.0, 20.0, 2.0));
        usageList.add(new Usage(LocalDateTime.of(2025, 1, 11, 19, 0), 18.5, 17.0, 1.5));
        usageList.add(new Usage(LocalDateTime.of(2025, 1, 12, 13, 30), 20.0, 18.0, 2.0));


        return usageList;
    }
}