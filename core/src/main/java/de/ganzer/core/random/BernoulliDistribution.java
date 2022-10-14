package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random boolean values, according to the discrete probability
 * function.
 */
public class BernoulliDistribution {
    /**
     * Creates a new instance with p set to 0.5.
     */
    public BernoulliDistribution() {
        this(0.5);
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param p The p distribution parameter (probability of generating true).
     */
    public BernoulliDistribution(double p) {
        this.p = p;
    }

    /**
     * Creates the next random value according to the set p.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random value.
     */
    public boolean next(Random random) {
        return false;
    }

    private double p;
}
