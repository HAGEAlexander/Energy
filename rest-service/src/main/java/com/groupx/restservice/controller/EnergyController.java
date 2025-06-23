package com.groupx.restservice.controller;

import com.groupx.restservice.model.CurrentPercentage;
import com.groupx.restservice.model.HourlyAggregate;
import com.groupx.restservice.service.EnergyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final EnergyService service;

    public EnergyController(EnergyService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public CurrentPercentage getCurrent() {
        return service.getCurrentPercentage();
    }

    @GetMapping("/historical")
    public List<HourlyAggregate> getHistorical(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ) {
        return service.getHistorical(start, end);
    }
}
