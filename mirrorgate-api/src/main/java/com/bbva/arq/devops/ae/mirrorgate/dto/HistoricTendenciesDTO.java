package com.bbva.arq.devops.ae.mirrorgate.dto;

public class HistoricTendenciesDTO {

    private double longTermTendency;

    private double midTermTendency;

    private double shortTermTendency;

    public HistoricTendenciesDTO(double longTermTendency, double midTermTendency, double shortTermTendency) {

        this.longTermTendency = longTermTendency;
        this.midTermTendency = midTermTendency;
        this.shortTermTendency = shortTermTendency;
    }

    public double getLongTermTendency() {
        return longTermTendency;
    }

    public void setLongTermTendency(double longTermTendency) {
        this.longTermTendency = longTermTendency;
    }

    public double getMidTermTendency() {
        return midTermTendency;
    }

    public void setMidTermTendency(double midTermTendency) {
        this.midTermTendency = midTermTendency;
    }

    public double getShortTermTendency() {
        return shortTermTendency;
    }

    public void setShortTermTendency(double shortTermTendency) {
        this.shortTermTendency = shortTermTendency;
    }
}
