package de.ganzer.core.random;

import java.util.Random;

/**
 * The interface to a random number distribution object.
 *
 * @param <T> The type of the number to produce.
 */
public interface Distribution<T extends Number> {
    /**
     * Creates the next distributed value.
     *
     * @param random The random number generator where to get uniformly
     *               distributed random numbers from.
     *
     * @return The next random value.
     */
    T next(Random random);
}
