package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers according to a Cauchy distribution (also called
 * Lorentz distribution).
 */
public class CauchyDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with location set ot 0.0 and scale set to 1.0.
     */
    public CauchyDistribution() {
        this(0.0, 1.0);
    }

    /**
     * Creates a new instance with scale set to 1.0.
     *
     * @param location The location.
     */
    public CauchyDistribution(double location) {
        this(location, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param location The location.
     * @param scale The scale.
     */
    public CauchyDistribution(double location, double scale) {
        this.location = location;
        this.scale = scale;
    }

    /**
     * Creates the next random number according to the set location and scale.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     * @return The next random number.
     */
    @Override
    public Double next(Random random) {
        return 0.0;
    }

    private double location;
    private double scale;
}
