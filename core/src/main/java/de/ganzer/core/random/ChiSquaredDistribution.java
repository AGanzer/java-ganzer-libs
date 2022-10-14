package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers x > 0 according to the Chi-squared distribution.
 */
public class ChiSquaredDistribution {
    /**
     * Creates a new instance with n set to 1.0.
     */
    public ChiSquaredDistribution() {
        this(1.0);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param n The n distribution parameter (degrees of freedom).
     */
    public ChiSquaredDistribution(double n) {
        this.n = n;
    }

    /**
     * Creates the next random number according to the set degrees of freedom.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    public double next(Random random) {
        return 0;
    }

    private double n;
}
