package de.ganzer.gv.core;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TerminalSize;

/**
 * Extends {@link TerminalRectangle}.
 */
public class Rectangle extends TerminalRectangle {
    /**
     * Creates a new terminal rect representation at the supplied position and
     * size.
     *
     * @param position The position of the rectangle.
     * @param size The size of the rectangle.
     */
    public Rectangle(TerminalPosition position, TerminalSize size) {
        super(position.getColumn(), position.getRow(), size.getColumns(), size.getRows());
    }

    /**
     * Creates a new terminal rect representation at the supplied x y position
     * with the supplied width and height.
     * <p>
     * Both width and height must be at least zero (non-negative) as checked in
     * {@link TerminalSize}.
     *
     * @param x The left position.
     * @param y The top position.
     * @param width The number of columns.
     * @param height The number of rows.
     */
    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Gets the position of the rectangle.
     *
     * @return The position.
     */
    public Position getPosition() {
        return new Position(x, y);
    }

    /**
     * Gets the size of the rectangle.
     *
     * @return The size.
     */
    public Size getSize() {
        return new Size(width, height);
    }

    /**
     * Gets a new rectangle that is relatively moved to this.
     *
     * @param dx The number of columns to move.
     * @param dy The number of rows to move.
     *
     * @return The new rectangle.
     */
    public Rectangle moveBy(int dx, int dy) {
        return new Rectangle(x + dx, y + dy, width, height);
    }
}
