package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random floating-point values x, uniformly distributed on the
 * interval [min,max).
 */
@SuppressWarnings("unused")
public class UniformFloatingPointDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with a minimum value of 0.0 and a maximum value of
     * 1.0.
     */
    public UniformFloatingPointDistribution() {
        this(0.0, 1.0);
    }

    /**
     * Creates a new instance with a minimum value of 0.0.
     *
     * @param max The maximum value to create.
     */
    public UniformFloatingPointDistribution(double max) {
        this(0.0, max);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param min The minimum value to create.
     * @param max The maximum value to create.
     *
     * @throws IllegalArgumentException min is greater than max.
     */
    public UniformFloatingPointDistribution(double min, double max) {
        if (min > max)
            throw new IllegalArgumentException("min,max");
        
        this.min = min;
        this.max = max;
    }

    /**
     * Creates the next random number in the range [min,max).
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        return random.nextDouble() * (max - min) + min;
    }

    private final double min;
    private final double max;
}
