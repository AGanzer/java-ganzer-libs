package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers according to the Normal (or Gaussian) random number
 * distribution.
 */
public class NormalDistribution {
    /**
     * Creates a new instance with a mean of 0.0 and a deviation of 1.0.
     */
    public NormalDistribution() {
        this(0.0, 1.0);
    }

    /**
     * Creates a new instance with a deviation of 1.0.
     *
     * @param mean The mean to use.
     */
    public NormalDistribution(double mean) {
        this(mean, 1.0);
    }

    /**
     * Creates a new instance from the specifeid arguments.
     *
     * @param mean      The mean to use.
     * @param deviation The devialtion to use.
     */
    public NormalDistribution(double mean, double deviation) {
        this.mean = mean;
        this.deviation = deviation;
    }

    /**
     * Creates the next random number according to the set mean and deviation.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random number.
     */
    public double next(Random random) {
        double result;

        if (hasNext) {
            hasNext = false;
            result = next;
        } else {
            double u;
            double v;
            double s;

            do {
                u = 2 * random.nextDouble() - 1;
                v = 2 * random.nextDouble() - 1;
                s = u * u + v * v;
            } while (s >= 1 || s == 0);

            double f = StrictMath.sqrt(-2 * StrictMath.log(s) / s);

            next = v * f;
            hasNext = true;
            result = u * f;
        }

        return result * deviation + mean;
    }

    private final double mean;
    private final double deviation;
    private boolean hasNext;
    private double next;
}
