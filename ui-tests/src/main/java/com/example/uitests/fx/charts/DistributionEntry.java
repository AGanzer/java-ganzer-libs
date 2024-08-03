package com.example.uitests.fx.charts;

public class DistributionEntry {
    private final String name;
    private final DistributionEnvelop<Double> distribution;

    public DistributionEntry(String name, DistributionEnvelop<Double> distribution) {
        this.name = name;
        this.distribution = distribution;
    }

    public DistributionEnvelop<Double> getDistribution() {
        return distribution;
    }

    @Override
    public String toString() {
        return name;
    }
}
