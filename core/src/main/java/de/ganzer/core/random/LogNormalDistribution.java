package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers  x > 0 according to a log-normal distribution.
 * <p>
 * The parameters m and s are, respectively, the mean and standard deviation of
 * the natural logarithm of x.
 */
@SuppressWarnings("unused")
public class LogNormalDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with m set to 0.0 and s set to 1.0.
     */
    public LogNormalDistribution() {
        this(0.0, 1.0);
    }

    /**
     * Creates a new instance with s set to 1.0.
     *
     * @param m The m distribution parameter (log-scale).
     */
    public LogNormalDistribution(double m) {
        this(m, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param m The m distribution parameter (log-scale).
     * @param s The s distribution parameter (shape).
     */
    public LogNormalDistribution(double m, double s) {
        normDist = new NormalDistribution(m, s);
    }

    /**
     * Creates the next random number according to the set m and s parameters.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        return StrictMath.exp(normDist.next(random));
    }

    private final NormalDistribution normDist;
}
