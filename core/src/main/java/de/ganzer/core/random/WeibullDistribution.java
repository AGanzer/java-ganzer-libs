package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers according to the Weibull distribution.
 */
public class WeibullDistribution {
    /**
     * Creates a new instance with shape and scale set to 1.0.
     */
    public WeibullDistribution() {
        this(1.0, 1.0);
    }

    /**
     * Creates a new instance with scale set to 1.0.
     *
     * @param shape The shape.
     */
    public WeibullDistribution(double shape) {
        this(shape, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param shape The shape.
     * @param scale The scale.
     */
    public WeibullDistribution(double shape, double scale) {
        this.shape = shape;
        this.scale = scale;
    }

    /**
     * Creates the next random number according to the set shape and scale.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    public double next(Random random) {
        return 0;
    }

    private double shape;
    private double scale;
}
