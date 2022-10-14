package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random exponential distributed non-negative integer values.
 * <p>
 * The value represents the number of yes/no trials (each succeeding with
 * probability p) which are necessary to obtain a single success.
 * <p>
 * {@code GeometricDistribution(p)} is exactly equivalent to
 * {@link NegativeBinomialDistribution}(1, p). It is also the discrete
 * counterpart of {@link ExponentialDistribution}.
 */
public class GeometricDistribution implements Distribution<Long> {
    /**
     * Creates a new instance with p set to 0.5.
     */
    public GeometricDistribution() {
        this(0.5);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param p The p distribution parameter (probability of a trial generating
     *          true). This must be in the range (0,1).
     */
    public GeometricDistribution(double p) {
        if (p <= 0 || 1 <= p)
            throw new IllegalArgumentException("p");

        this.p = p;
    }

    /**
     * Creates the next random number according to the set p.
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

    private final double p;
}
