package com.groupx.usage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_aggregates")
public class HourlyAggregate {
    @Id
    private LocalDateTime hour;
    private double communityProduced = 0.0;
    private double communityUsed = 0.0;
    private double gridUsed = 0.0;

    public HourlyAggregate() {}
    public HourlyAggregate(LocalDateTime hour) { this.hour = hour; }

    // getters & setters
    public LocalDateTime getHour() {
        return hour;
    }

    public void setHour(LocalDateTime hour) {
        this.hour = hour;
    }

    public double getCommunityProduced() {
        return communityProduced;
    }

    public void setCommunityProduced(double communityProduced) {
        this.communityProduced = communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public void setCommunityUsed(double communityUsed) {
        this.communityUsed = communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }

    public void setGridUsed(double gridUsed) {
        this.gridUsed = gridUsed;
    }
}