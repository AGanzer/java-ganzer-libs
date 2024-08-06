package de.ganzer.swing.validaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ValidationFilterList implements Iterable<ValidationFilter> {
    private final List<ValidationFilter> filters = new ArrayList<>();

    public void addFilter(ValidationFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null.");
        filters.add(filter);
    }

    public void removeFilter(ValidationFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null.");
        filters.remove(filter);
    }

    public boolean validate(ValidationBehavior behavior) {
        for (var filter: this)
            if (!filter.validate(behavior))
                return false;

        return true;
    }

    @Override
    public Iterator<ValidationFilter> iterator() {
        return filters.iterator();
    }
}
