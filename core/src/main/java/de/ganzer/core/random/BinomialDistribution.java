package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random non-negative integer values.
 * <p>
 * The value obtained is the number of successes in a sequence of t yes/no
 * experiments, each of which succeeds with probability p.
 */
public class BinomialDistribution {
    /**
     * Creates a new instance with t set to 1 and p set to 0.5.
     */
    public BinomialDistribution() {
        this(1, 0.5);
    }

    /**
     * Creates a new instance with p set to 0.5.
     *
     * @param t The t distribution parameter (number of trial).
     */
    public BinomialDistribution(long t) {
        this(t, 0.5);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param t The t distribution parameter (number of trial).
     * @param p The p distribution parameter (probability of a trial generating
     *          true).
     */
    public BinomialDistribution(long t, double p) {
        this.t = t;
        this.p = p;
    }

    /**
     * Creates the next random number according to the set t and p.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    long next(Random random) {
        return 0;
    }

    private long t;
    private double p;

}
