package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random exponential distributed non-negative numbers.
 * <p>
 * The value obtained is the time/distance until the next random event if random
 * events occur at constant rate λ per unit of time/distance. For example, this
 * distribution describes the time between the clicks of a Geiger counter or the
 * distance between point mutations in a DNA strand.
 * <p>
 * This is the continuous counterpart of {@link GeometricDistribution}.
 */
public class ExponentialDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with lambda set to 1.0.
     */
    public ExponentialDistribution() {
        this(1.0);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param lambda The λ distribution parameter (the rate parameter).
     */
    public ExponentialDistribution(double lambda) {
        this.lambda = lambda;
    }

    /**
     * Creates the next random number according to the set lambda.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        return StrictMath.log(1 - random.nextDouble()) / lambda;
    }

    private final double lambda;
}
