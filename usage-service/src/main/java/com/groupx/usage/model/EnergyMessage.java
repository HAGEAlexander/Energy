package com.groupx.usage.model;

import java.time.OffsetDateTime;

public class EnergyMessage {
    private String type;
    private String association;
    private double kwh;
    private OffsetDateTime datetime;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAssociation() { return association; }
    public void setAssociation(String association) { this.association = association; }

    public double getKwh() { return kwh; }
    public void setKwh(double kwh) { this.kwh = kwh; }

    public OffsetDateTime getDatetime() { return datetime; }
    public void setDatetime(OffsetDateTime datetime) { this.datetime = datetime; }
}
