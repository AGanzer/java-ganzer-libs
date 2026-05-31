package de.ganzer.fx.validation;

import java.util.*;

/**
 * A utility class that simplifies the validation of multiple inputs.
 * <p>
 * <p>
 * This is useful to validate a dialog with many validators. Instead of calling
 * each validator separately, {@link #validate(ValidationBehavior)} of this
 * collection can be called to validate the complete input.
 *
 * @since 5.4.0
 */
@SuppressWarnings("unused")
public class ValidationTextFormatterList implements Iterable<ValidationTextFormatter> {
    private final List<ValidationTextFormatter> formatters = new ArrayList<>();

    /**
     * Adds the specified formatter to this collection.
     *
     * @param formatter The formatter to add.
     *
     * @throws NullPointerException {@code formatter} is {@code null}.
     *
     * @since 5.6.0
     */
    public void addFormatter(ValidationTextFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null.");
        formatters.add(formatter);
    }

    /**
     * Removes the specified formatter from this collection.
     *
     * @param formatter The formatter to remove.
     *
     * @throws NullPointerException {@code formatter} is {@code null}.
     *
     * @since 5.6.0
     */
    public void removeFormatter(ValidationTextFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null.");
        formatters.remove(formatter);
    }

    /**
     * Adds the specified formatter to this collection.
     *
     * @param formatter The formatter to add.
     *
     * @throws NullPointerException {@code formatter} is {@code null}.
     *
     * @deprecated Use {@link #addFormatter(ValidationTextFormatter)} instead.
     */
    @Deprecated
    public void addFilter(ValidationTextFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null.");
        formatters.add(formatter);
    }

    /**
     * Removes the specified formatter from this collection.
     *
     * @param formatter The formatter to remove.
     *
     * @throws NullPointerException {@code formatter} is {@code null}.
     *
     * @deprecated Use {@link #removeFormatter(ValidationTextFormatter)} instead.
     */
    @Deprecated
    public void removeFilter(ValidationTextFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null.");
        formatters.remove(formatter);
    }

    /**
     * Invokes {@link ValidationTextFormatter#validate(ValidationBehavior)} for each
     * contained filter.
     * <p>
     * How this method works depends on {@code behavior}. If it is
     * {@link ValidationBehavior#THROW_EXCEPTION} or
     * {@link ValidationBehavior#SHOW_MESSAGE_BOX}, the validation stops at
     * the first fail. If {@code behavior} is
     * {@link ValidationBehavior#SET_VISUAL_HINTS}, all inputs are validated and
     * each invalid input is marked with a visuel hint.
     *
     * @param behavior The behavior to use for validation.
     *
     * @return {@code true} if all input is valid; otherwise, {@code false}.
     *         Remark that there is no return if {@code behavior} is
     *         {@link ValidationBehavior#THROW_EXCEPTION}.
     */
    public boolean validate(ValidationBehavior behavior) {
        boolean valid = true;

        for (var formatter: this) {
            if (!formatter.validate(behavior)) {
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
    public Iterator<ValidationTextFormatter> iterator() {
        return formatters.iterator();
    }
}
