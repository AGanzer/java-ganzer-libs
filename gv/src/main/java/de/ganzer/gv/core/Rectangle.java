package de.ganzer.gv.core;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalRectangle;
import com.googlecode.lanterna.TerminalSize;

/**
 * Extends {@link TerminalRectangle}.
 */
public class Rectangle extends TerminalRectangle {
    /**
     * An empty rectangle where all properties are set to 0.
     */
    public static final Rectangle EMPTY = new Rectangle(0, 0, 0, 0);

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
     * Gets a value indicating whether the size is not zero and not negative.
     *
     * @return {@code true} if {@link #width} and {@link #height} are both
     *         greater than 0; otherwise, {@code false}.
     */
    public boolean isExposed() {
        return width > 0 && height > 0;
    }

    /**
     * Determines if the specified position is within the bounds of this
     * rectangle.
     *
     * @param position The position to check.
     *
     * @return {@code true} if the specified point is within the bounds of this
     *         rectangle; otherwise, {@code false}.
     */
    public boolean contains(TerminalPosition position) {
        return x <= position.getColumn()
                && position.getColumn() < xAndWidth
                && y <= position.getRow()
                && position.getRow() < yAndHeight;
    }

    /**
     * Determines if the specified position is within the bounds of this
     * rectangle.
     *
     * @param xPos The X-position the check.
     * @param yPos The Y-position the check.
     *
     * @return {@code true} if the specified point is within the bounds of this
     *         rectangle; otherwise, {@code false}.
     */
    public boolean contains(int xPos, int yPos) {
        return x <= xPos && xPos < xAndWidth && y <= yPos && yPos < yAndHeight;
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

    /**
     * Gets a new rectangle that is relatively grown to this.
     * <p>
     * Expanding a rectangle does not affect the position but does resize the
     * new rectangle only.
     * <p>
     * A negative amount shrinks the size of the new rectangle.
     *
     * @param dx The number of columns to grow.
     * @param dy The number of rows to grow.
     *
     * @return The new rectangle.
     */
    public Rectangle grownBy(int dx, int dy) {
        return new Rectangle(x, y, width + dx, height + dy);
    }

    /**
     * Gets a new rectangle that is relatively inflated to the bounds of this
     * rectangle by the specified amount.
     * <p>
     * Inflating a rectangle affects all edges. The position of the rectangle's
     * center remains unchanged.
     * <p>
     * A negative amount deflates the rectangle.
     *
     * @param amount The number of rows and columns to inflate.
     *
     * @return The new rectangle.
     */
    public Rectangle inflatedBy(int amount) {
        return new Rectangle(x - amount, y - amount, width + amount, height + amount);
    }

    /**
     * Gets a new rectangle that is relatively inflated to the bounds of this
     * rectangle by the specified amounts.
     * <p>
     * Inflating a rectangle affects all edges. The position of the rectangle's
     * center remains unchanged.
     * <p>
     * A negative amount deflates the rectangle.
     *
     * @param dx The number of columns to inflate.
     * @param dy The number of rows to inflate.
     *
     * @return The new rectangle.
     */
    public Rectangle inflatedBy(int dx, int dy) {
        return new Rectangle(x - dx, y - dy, width + dx, height + dy);
    }

    /**
     * Gets a new rectangle that contains the result of the intersection of this
     * rectangle with the specified one.
     *
     * @param other The rectangle where to intersect this rectangle with.
     *
     * @return The new rectangle that covers the overlapping regions of this
     *         and {@code other}.
     */
    public Rectangle intersectedBy(Rectangle other) {
        int left = Math.max(x, other.x);
        int top = Math.max(y, other.y);
        int right = Math.min(xAndWidth, other.xAndWidth);
        int bottom = Math.min(yAndHeight, other.yAndHeight);

        return new Rectangle(left, top, right - left, bottom - top);
    }

    /**
     * Gets a new rectangle that contains the result of the union of this
     * rectangle with the specified one.
     *
     * @param other The rectangle where to unite this rectangle with.
     *
     * @return A new rectangle that covers the combined regions of this and
     *         {@code other}.
     */
    public Rectangle unitedWith(Rectangle other) {
        int left = Math.min(x, other.x);
        int top = Math.min(y, other.y);
        int right = Math.max(xAndWidth, other.xAndWidth);
        int bottom = Math.max(yAndHeight, other.yAndHeight);

        return new Rectangle(left, top, right - left, bottom - top);
    }

    /**
     * Compares the rectangle with the specified object.
     *
     * @param obj The other rectangle to compare with.
     *
     * @return {@code true} if this is equal to {@code other}; otherwise,
     *         {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof TerminalRectangle other))
            return false;

        return x == other.x && y == other.y && width == other.width;
    }
}
