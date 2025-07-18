package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random exponential distributed non-negative integer values.
 * <p>
 * The value represents the number of failures in a series of independent yes/no
 * trials (each succeeds with probability p), before exactly k successes occur.
 */
@SuppressWarnings("unused")
public class NegativeBinomialDistribution implements Distribution<Long> {
    /**
     * Creates a new instance with k set to 1 and p set to 0.5.
     */
    public NegativeBinomialDistribution() {
        this(1, 0.5);
    }

    /**
     * Creates a new instance with p set to 0.5.
     *
     * @param k The k distribution parameter (number of trial successes).
     */
    public NegativeBinomialDistribution(long k) {
        this(k, 0.5);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param k The k distribution parameter (number of trial successes).
     * @param p The p distribution parameter (probability of a trial generating
     *          true).
     *
     * @throws IllegalArgumentException p is greater than 1 or p is less than or
     * equal to 0.
     */
    public NegativeBinomialDistribution(long k, double p) {
        if (p > 1.0 || p <= 0.0)
            throw new IllegalArgumentException("p");

        this.k = k;
        this.p = p;

        if (k <= 21 * p)
            bernDist = new BernoulliDistribution(p);
        else
            gammaDist = new GammaDistribution(k, (1 - p) / p);
    }

    /**
     * Creates the next random number according to the set k and p.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Long next(Random random) {
        if (k <= 21 * p) {
            long f = 0;
            long s = 0;

            while (s < k) {
                if (bernDist.next(random))
                    ++s;
                else
                    ++f;
            }

            return f;
        }

        return new PoissonDistribution(gammaDist.next(random)).next(random);
    }

    private final long k;
    private final double p;
    private BernoulliDistribution bernDist;
    private GammaDistribution gammaDist;
}
