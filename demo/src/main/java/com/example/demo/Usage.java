package com.example.demo;

import java.time.LocalDateTime;

public class Usage {
    private LocalDateTime hour;
    private Double communityProduced;
    private Double communityUsed;
    private Double gridUsed;

    public LocalDateTime getHour() { return hour; }
    public Double getCommunityProduced() { return communityProduced; }
    public Double getCommunityUsed() { return communityUsed; }
    public Double getGridUsed() { return gridUsed; }

    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public void setCommunityProduced(Double communityProduced) { this.communityProduced = communityProduced; }
    public void setCommunityUsed(Double communityUsed) { this.communityUsed = communityUsed; }
    public void setGridUsed(Double gridUsed) { this.gridUsed = gridUsed; }
}
