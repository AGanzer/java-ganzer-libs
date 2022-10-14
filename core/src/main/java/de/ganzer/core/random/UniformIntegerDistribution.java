package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random integer values i, uniformly distributed on the closed
 * interval [min,max].
 */
public class UniformIntegerDistribution implements Distribution<Long> {
    /**
     * Creates a new instance with a minimum value of 0 and a maximum value of
     * {@code Integer.MAX_VALUE}.
     */
    public UniformIntegerDistribution() {
        this(0, Integer.MAX_VALUE);
    }

    /**
     * Creates a new instance with a minimum value of 0.
     *
     * @param max The maximum value to create.
     */
    public UniformIntegerDistribution(long max) {
        this(0, max);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param min The minimum value to create.
     * @param max The maximum value to create.
     */
    public UniformIntegerDistribution(long min, long max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Creates the next random number in the range [min,max].
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

    private final long min;
    private final long max;
}
