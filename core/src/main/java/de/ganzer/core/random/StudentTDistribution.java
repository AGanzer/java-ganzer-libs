package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random floating-point values, distributed according a probability
 * density function.
 * <p>
 * This distribution is used when estimating the mean of an unknown normally
 * distributed value given n + 1 independent measurements, each with additive
 * errors of unknown standard deviation, as in physical measurements. Or,
 * alternatively, when estimating the unknown mean of a normal distribution
 * with unknown standard deviation, given n + 1 samples.
 */
public class StudentTDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with n set to 1.0.
     */
    public StudentTDistribution() {
        this(1.0);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param n The n distribution parameter (degrees of freedom).
     */
    public StudentTDistribution(double n) {
        this.n = n;
        gammaDist = new GammaDistribution(n * 0.5, 2);
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
        return normDist.next(random) * StrictMath.sqrt(n / gammaDist.next(random));
    }

    private final double n;
    private final GammaDistribution gammaDist;
    private final NormalDistribution normDist = new NormalDistribution();
}
