package com.example.demo;

import java.time.LocalDateTime;

public class CurrentPercentage {
    private LocalDateTime hour;
    private Double communityDepleted;
    private Double gridPortion;

    public LocalDateTime getHour() { return hour; }
    public Double getCommunityDepleted() { return communityDepleted; }
    public Double getGridPortion() { return gridPortion; }

    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public void setCommunityDepleted(Double communityDepleted) { this.communityDepleted = communityDepleted; }
    public void setGridPortion(Double gridPortion) { this.gridPortion = gridPortion; }
}
