package de.ganzer.swing.validaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A collection of {@link ValidationFilter} objects.
 * <p>
 * This is useful to validate a dialog with many validators. Instead of calling
 * each validator separately, {@link #validate(ValidationBehavior)} of this
 * collection can be called to validate the complete input.
 */
@SuppressWarnings("unused")
public class ValidationFilterList implements Iterable<ValidationFilter> {
    private final List<ValidationFilter> filters = new ArrayList<>();

    /**
     * Adds the specified filter to this collection.
     *
     * @param filter The filter to add.
     *
     * @throws NullPointerException {@code filter} is {@code null}.
     */
    public void addFilter(ValidationFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null.");
        filters.add(filter);
    }

    /**
     * Removes the specified filter from this collection.
     *
     * @param filter The filter to remove.
     *
     * @throws NullPointerException {@code filter} is {@code null}.
     */
    public void removeFilter(ValidationFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null.");
        filters.remove(filter);
    }

    /**
     * Invokes {@link ValidationFilter#validate(ValidationBehavior)} for each
     * contained filter.
     * <p>
     * How this method works depends on {@code behavior}. If it is
     * {@link ValidationBehavior#THROW_EXCEPTION} or
     * {@link ValidationBehavior#SHOW_MESSAGE_BOX}, the validation stops at
     * the first fail. If {@code behavior} is
     * {@link ValidationBehavior#SET_VISUAL_HINTS}, all inputs are validated and
     * each invalid input is marked with a visuell hint.
     *
     * @param behavior The behavior to use for validation.
     *
     * @return {@code true} if all input is valid; otherwise, {@code false}.
     *         Remark that there is no return if {@code behavior} is
     *         {@link ValidationBehavior#THROW_EXCEPTION}.
     */
    public boolean validate(ValidationBehavior behavior) {
        boolean valid = true;

        for (var filter: this) {
            if (!filter.validate(behavior)) {
                if (behavior == ValidationBehavior.SHOW_MESSAGE_BOX)
                    return false;

                valid = false;
            }
        }

        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ValidationFilter> iterator() {
        return filters.iterator();
    }
}
