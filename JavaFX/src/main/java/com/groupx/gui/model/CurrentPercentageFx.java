package com.groupx.gui.model;

public class CurrentPercentageFx {
    private final double communityDepleted;
    private final double gridPortion;

    public CurrentPercentageFx(double communityDepleted, double gridPortion) {
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
    }

    public double getCommunityDepleted() { return communityDepleted; }
    public double getGridPortion() { return gridPortion; }
}
