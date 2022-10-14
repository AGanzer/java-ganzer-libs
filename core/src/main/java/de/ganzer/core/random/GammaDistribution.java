package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random positive floating-point values.
 * <p>
 * For floating-point α, the value obtained is the sum of α independent
 * exponentially distributed random variables, each of which has a mean of β.
 */
public class GammaDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with shape and scale set to 1.0.
     */
    public GammaDistribution() {
        this(1.0, 1.0);
    }

    /**
     * Creates a new instance with scale set to 1.0.
     *
     * @param shape The shape.
     */
    public GammaDistribution(double shape) {
        this(shape, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param shape The shape.
     * @param scale The scale.
     */
    public GammaDistribution(double shape, double scale) {
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
        return .0;
    }

    private final double shape;
    private final double scale;
}
