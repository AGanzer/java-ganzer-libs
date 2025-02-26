package de.ganzer.gv.core;

import com.googlecode.lanterna.TerminalSize;

public class Size extends TerminalSize {
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
     * Gets a new size that is relatively grown to this.
     *
     * @param columns The number of columns to grow.
     * @param rows The number of rows to grow.
     *
     * @return The new size.
     */
    public Size grownBy(int columns, int rows) {
        return new Size(getColumns() + columns, getRows() + rows);
    }
}
