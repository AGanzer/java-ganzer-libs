package de.ganzer.core;

import java.util.Objects;

public class Pair<F, S> {
    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Pair<?, ?>))
            return false;

        Pair<?, ?> op = (Pair<?, ?>) o;

        return Objects.equals(first, op.first) && Objects.equals(second, op.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
