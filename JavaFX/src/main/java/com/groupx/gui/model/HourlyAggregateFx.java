package com.groupx.gui.model;

import java.time.LocalDateTime;

public class HourlyAggregateFx {
    private final String hour;
    private final double communityProduced;
    private final double communityUsed;
    private final double gridUsed;

    public HourlyAggregateFx(LocalDateTime hour,
                             double communityProduced,
                             double communityUsed,
                             double gridUsed) {
        this.hour = hour.toString();
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public String getHour() { return hour; }
    public double getCommunityProduced() { return communityProduced; }
    public double getCommunityUsed() { return communityUsed; }
    public double getGridUsed() { return gridUsed; }
}
