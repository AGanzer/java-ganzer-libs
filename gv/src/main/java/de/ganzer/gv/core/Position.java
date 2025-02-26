package de.ganzer.gv.core;

import com.googlecode.lanterna.TerminalPosition;

public class Position extends TerminalPosition {
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
}
