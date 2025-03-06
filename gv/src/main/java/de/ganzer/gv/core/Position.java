package de.ganzer.gv.core;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;

public class Position extends TerminalPosition {
    /**
     * A position where all properties are set to 0.
     */
    public static final Position NULL = new Position(0, 0);

    /**
     * Creates a new TerminalPosition object, which represents a location on the
     * screen. There is no check to verify that the position you specified is
     * within the size of the current terminal, and you can specify negative
     * positions as well.
     *
     * @param column Column of the location, or the "x" coordinate, zero indexed
     *        (the first column is 0).
     * @param row Row of the location, or the "y" coordinate, zero indexed
     *        (the first row is 0).
     */
    public Position(int column, int row) {
        super(column, row);
    }

    /**
     * Converts this position to a size.
     *
     * @return The size that corresponds to this position.
     */
    public Size toSize() {
        return new Size(getColumn(), getRow());
    }

    /**
     * Gets a new position that is relatively moved to this.
     *
     * @param dx The number of columns to move.
     * @param dy The number of rows to move.
     *
     * @return The new position.
     */
    public Position movedBy(int dx, int dy) {
        return new Position(getColumn() + dx, getRow() + dy);
    }

    /**
     * Gets a new position that is relatively moved to this.
     *
     * @param delta The number of columns and rows to move.
     *
     * @return The new position.
     */
    public Position movedBy(TerminalPosition delta) {
        return new Position(getColumn() + delta.getColumn(), getRow() + delta.getRow());
    }

    /**
     * Gets a new position that is relatively moved to this.
     *
     * @param delta The number of columns and rows to move.
     *
     * @return The new position.
     */
    public Position movedBy(TerminalSize delta) {
        return new Position(getColumn() + delta.getColumns(), getRow() + delta.getRows());
    }

    /**
     * Compares the position with the specified object.
     *
     * @param obj The other position to compare with.
     *
     * @return {@code true} if this is equal to {@code other}; otherwise,
     *         {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof TerminalPosition other))
            return false;

        return getColumn() == other.getColumn() && getRow() == other.getRow();
    }
}
