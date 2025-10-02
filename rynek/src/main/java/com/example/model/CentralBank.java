package com.example.model;

import com.example.observer.Observer;

public class CentralBank implements Observer {
    private double inflation = 0.1;
    private double targetTaxRevenue;
    private double lastTurnover = 0.0;

    public CentralBank(double targetTaxRevenue) {
        this.targetTaxRevenue = targetTaxRevenue;
    }

    @Override
    public void update() {
        adjustInflation();
    }

    private void adjustInflation() {
        double currentTaxRevenue = inflation * lastTurnover;
        double delta = 0.0;
        if (lastTurnover > 0) {
            delta = (targetTaxRevenue - currentTaxRevenue) / lastTurnover;
        }
        // Smooth adjustment based on tax revenue difference
        inflation += 0.05 * delta;
    }

    public double getInflation() {
        return inflation;
    }

    public void setInflation(double newInflation) {
        this.inflation = newInflation;
    }

    public double getLastTurnover() {
        return lastTurnover;
    }

    public double getTargetTaxRevenue() {
        return targetTaxRevenue;
    }

    public void setLastTurnover(double turnover) {
        this.lastTurnover = turnover;
    }
}