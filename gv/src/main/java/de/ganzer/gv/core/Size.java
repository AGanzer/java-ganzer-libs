package de.ganzer.gv.core;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;

public class Size extends TerminalSize {
    /**
     * An empty size where all properties are set to 0.
     */
    public static final Size EMPTY = new Size(0, 0);

    /**
     * Creates a new terminal size representation with a given width (columns)
     * and height (rows).
     *
     * @param columns Width, in number of columns.
     * @param rows Height, in number of columns.
     */
    public Size(int columns, int rows) {
        super(columns, rows);
    }

    /**
     * Converts this size to a position.
     *
     * @return The position that corresponds to this size.
     */
    public Position toPosition() {
        return new Position(getColumns(), getRows());
    }

    /**
     * Gets a value indicating whether the size is not zero and not negative.
     *
     * @return {@code true} if {@link #getColumns()} and {@link #getRows()} are
     *         both greater than 0; otherwise, {@code false}.
     */
    public boolean isExposed() {
        return getColumns() > 0 && getRows() > 0;
    }

    /**
     * Creates a new size that is a copy of this size where the width and the
     * height are increased by the specified amount.
     * <p>
     * A negative amount decreases the width and the height of the returned
     * object.
     *
     * @param amount The amount where to increase the width and the height of
     *        the returned object by.
     *
     * @return The new size.
     */
    public Size increasedBy(int amount) {
        return new Size(getColumns() + amount, getRows() + amount);
    }

    /**
     * Creates a new size that is a copy of this size where the width and the
     * height are increased by the specified amount.
     * <p>
     * A negative amount decreases the width and the height of the returned
     * object.
     *
     * @param amount The amount where to increase the width and the height of
     *        the returned object by.
     *
     * @return The new size.
     */
    public Size increasedBy(TerminalPosition amount) {
        return new Size(getColumns() + amount.getColumn(), getRows() + amount.getRow());
    }

    /**
     * Creates a new size that is a copy of this size where the width and the
     * height are increased by the specified amount.
     * <p>
     * A negative amount decreases the width and the height of the returned
     * object.
     *
     * @param amount The amount where to increase the width and the height of
     *        the returned object by.
     *
     * @return The new size.
     */
    public Size increasedBy(TerminalSize amount) {
        return new Size(getColumns() + amount.getColumns(), getRows() + amount.getRows());
    }

    /**
     * Creates a new size that is a copy of this size where the width and the
     * height are increased by the specified amounts.
     * <p>
     * A negative amount decreases the width and the height of the returned
     * object.
     *
     * @param columns The number of columns to increase.
     * @param rows The number of rows to increase.
     *
     * @return The new size.
     */
    public Size increasedBy(int columns, int rows) {
        return new Size(getColumns() + columns, getRows() + rows);
    }
}
