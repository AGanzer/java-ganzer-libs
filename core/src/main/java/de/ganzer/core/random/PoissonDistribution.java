package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random non-negative integer values.
 * <p>
 * The value obtained is the probability of exactly i occurrences of a random
 * event if the expected, mean number of its occurrence under the same
 * conditions (on the same time/space interval) is mean.
 */
public class PoissonDistribution implements Distribution<Long> {
    /**
     * Creates a new instance with mean set to 1.0.
     */
    public PoissonDistribution() {
        this(1.0);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param mean The mean (µ). The must be greater than 0.0.
     */
    public PoissonDistribution(double mean) {
        if (mean <= 0)
            throw new IllegalArgumentException("mean");

        this.mean = mean;
    }

    /**
     * Creates the next random number according to the set mean.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    @Override
    public Long next(Random random) {
        return 0L;
    }

    private double mean;
}
