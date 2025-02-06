package de.ganzer.core;

import java.util.Objects;

/**
 * Defines a pair ov values.
 *
 * @param <F> The type of the first value.
 * @param <S> The type of the second value.
 */
public class Pair<F, S> {
    private final F first;
    private final S second;

    /**
     * Creates a new instance.
     *
     * @param first The first value to set.
     * @param second The second value to set.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first value.
     *
     * @return The value that was set at construction.
     */
    public F getFirst() {
        return first;
    }

    /**
     * Gets the second value.
     *
     * @return The value that was set at construction.
     */
    public S getSecond() {
        return second;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Pair<?, ?> op))
            return false;

        return Objects.equals(first, op.first) && Objects.equals(second, op.second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
