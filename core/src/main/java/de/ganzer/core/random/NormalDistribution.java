package de.ganzer.core.random;

public class NormalDistribution {
    public NormalDistribution() {
        this(0.0, 1.0);
    }

    public NormalDistribution(double mean) {
        this(mean, 1.0);
    }

    public NormalDistribution(double mean, double deviation) {
        this.mean = mean;
        this.deviation = deviation;
    }

    public double next() {
        return 0.0;
    }

    private double mean;
    private double deviation;
}
