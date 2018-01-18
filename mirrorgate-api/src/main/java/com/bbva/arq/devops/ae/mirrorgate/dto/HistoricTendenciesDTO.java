package com.bbva.arq.devops.ae.mirrorgate.dto;

public class HistoricTendenciesDTO {

    private double longTermTendency;

    private double shortTermTendency;


    public double getLongTermTendency() {
        return longTermTendency;
    }

    public void setLongTermTendency(double longTermTendency) {
        this.longTermTendency = longTermTendency;
    }

    public double getShortTermTendency() {
        return shortTermTendency;
    }

    public void setShortTermTendency(double shortTermTendency) {
        this.shortTermTendency = shortTermTendency;
    }
}
