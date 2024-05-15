package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers according to the Weibull distribution.
 */
@SuppressWarnings("unused")
public class WeibullDistribution implements Distribution<Double> {
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
     *
     * @throws IllegalArgumentException if shape is less than or equal to 0.0.
     */
    public WeibullDistribution(double shape, double scale) {
        if (shape <= 0.0)
            throw new IllegalArgumentException("shape");

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
    @Override
    public Double next(Random random) {
        return scale * StrictMath.pow(-StrictMath.log(1 - random.nextDouble()), 1.0 / shape);
    }

    private final double shape;
    private final double scale;
}
