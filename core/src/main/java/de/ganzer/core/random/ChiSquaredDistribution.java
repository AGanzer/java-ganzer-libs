package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers x > 0 according to the Chi-squared distribution.
 * <p>
 * {@code ChiSquaredDistribution(p)} is exactly equivalent to
 * {@link GammaDistribution}(n / 2, 2).
 */
public class ChiSquaredDistribution implements Distribution<Double> {
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
        gammaDist = new GammaDistribution(n / 2, 2);
    }

    /**
     * Creates the next random number according to the set degrees of freedom.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        return gammaDist.next(random);
    }

    private final GammaDistribution gammaDist;
}
