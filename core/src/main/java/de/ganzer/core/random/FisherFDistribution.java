package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers according to the f-distribution.
 */
@SuppressWarnings("unused")
public class FisherFDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with m and n set to 1.0.
     */
    public FisherFDistribution() {
        this(1.0, 1.0);
    }

    /**
     * Creates a new instance with n set to 1.0.
     *
     * @param m The m distribution parameter (degrees of freedom).
     */
    public FisherFDistribution(double m) {
        this(m, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param m The m distribution parameter (degrees of freedom).
     * @param n The n distribution parameter (degrees of freedom).
     */
    public FisherFDistribution(double m, double n) {
        this.m = m;
        this.n = n;
        gammaDistM = new GammaDistribution(m * 0.5);
        gammaDistN = new GammaDistribution(n * 0.5);
    }

    /**
     * Creates the next random number according to the set m and n parameters.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        return n * gammaDistM.next(random) / (m * gammaDistN.next(random));
    }

    private final double m;
    private final double n;
    private final GammaDistribution gammaDistM;
    private final GammaDistribution gammaDistN;
}
