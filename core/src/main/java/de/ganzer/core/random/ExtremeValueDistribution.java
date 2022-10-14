package de.ganzer.core.random;

import java.util.Random;

/**
 * Produces random numbers according to the extreme value distribution.
 * <p>
 * This it is also known as Gumbel Type I, log-Weibull or Fisher-Tippett Type I.
 */
public class ExtremeValueDistribution implements Distribution<Double> {
    /**
     * Creates a new instance with location set ot 0.0 and scale set to 1.0.
     */
    public ExtremeValueDistribution() {
        this(0.0, 1.0);
    }

    /**
     * Creates a new instance with scale set to 1.0.
     *
     * @param location The location.
     */
    public ExtremeValueDistribution(double location) {
        this(location, 1.0);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param location The location.
     * @param scale The scale.
     */
    public ExtremeValueDistribution(double location, double scale) {
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
        return .0;
    }

    private final double location;
    private final double scale;
}
