package de.ganzer.gv.devices;

import com.googlecode.lanterna.TextCharacter;
import de.ganzer.gv.core.Rectangle;

/**
 * IDC defines the interface of an object that encapsulates a device context
 * for drawing.
 */
public interface DC {
    /**
     * Gets a value indicating whether the DC is locked.
     * <p>
     * A DC is locked if the internal lock counter is greater than 0. The lock
     * counter is changed by {@link #lock(boolean)}.
     * <p>
     * Locking a DC locks the target device, not the context object itself. It
     * relates to all context objects that access the same device.
     *
     * @return {@code true} if the DC is locked.
     */
    boolean isLocked();

    /**
     * Gets the extent of the device. The left and top edges are always zero.
     *
     * @return The extent of the device.
     *
     * @see #getClipRect()
     */
    Rectangle getExtent();

    /**
     * Gets or sets the clipping region.
     *
     * @return The current clipping region.
     */
    Rectangle getClipRect();

    /**
     * Sets the clipping region.
     * <p>
     * All output is done within the clipping region only. If a clipping region
     * is set that exceeds the extent of the device, the clipping region is
     * adjusted (this may result into an empty clipping region).
     * <p>
     * The default clipping region is equal to {@link #getExtent()}.
     *
     * @param clipRect The clipping region to set.
     *
     * @throws NullPointerException {@code clipRect} is {@code null}.
     */
    void setClipRect(Rectangle clipRect);

    /**
     * Increments or decrements a lock counter.
     * <p>
     * The DC is buffered as long as the lock counter is greater than 0. The
     * counter is incremented if {@code increment} is {@code true}; otherwise,
     * the counter is decremented. All buffered changes are written into the
     * terminal if the counter reaches 0.
     * <p>
     * Locking a DC locks the device, not the context object itself. It relates
     * to all context objects that access the same device.
     *
     * @param increment If this is {@code true}, the lock counter is incremented;
     *        otherwise, it is decremented.
     *
     * @return The changed counter value.
     *
     * @throws IllegalStateException If the counter 0 and {@code increment} is
     *         {@code false}.
     */
    int lock(boolean increment);

    /**
     * Fills the specified region with the given attributes. The characters are
     * not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param bounds The region to fill.
     * @param attr The attributes where to fill the region with.
     *
     * @throws NullPointerException {@code bounds} or {@code ch} is
     *         {@code null}.
     *
     * @see #getClipRect()
     */
    void fillAttr(Rectangle bounds, TextCharacter attr);

    /**
     * Fills {@code count} cells of the specified line with the given attributes,
     * starting at {@code column}. The characters  are not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param column The zero-based column where to start.
     * @param row The zero-based line to fill.
     * @param count The number of columns to fill. If this is less than 1,
     *        nothing is done.
     * @param attr The attributes where to fill the region with.
     *
     * @throws NullPointerException {@code ch} is {@code null}.
     *
     * @see #getClipRect()
     */
    void fillAttr(int column, int row, int count, TextCharacter attr);

    /**
     * Fills the specified region with the given character. The attributes are
     * not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param bounds The region to fill.
     * @param ch The character where to fill the region with.
     *
     * @throws NullPointerException {@code bounds} is {@code null}.
     *
     * @see #getClipRect()
     */
    void fillChar(Rectangle bounds, char ch);

    /**
     * Fills {@code count} cells of the specified line with the given character,
     * starting at {@code column}. The attributes  are not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param column The zero-based column where to start.
     * @param row The zero-based line to fill.
     * @param count The number of columns to fill. If this is less than 1,
     *        nothing is done.
     * @param ch The character where to fill the region with.
     *
     * @see #getClipRect()
     */
    void fillChar(int column, int row, int count, char ch);

    /**
     * Fills the specified region with the given character. The attributes are
     * not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param bounds The region to fill.
     * @param ch The character where to fill the region with.
     *
     * @throws NullPointerException {@code bounds} or {@code ch} is
     *         {@code null}.
     *
     * @see #getClipRect()
     */
    void fill(Rectangle bounds, TextCharacter ch);

    /**
     * Fills {@code count} cells of the specified line with the given character,
     * starting at {@code column}. The attributes  are not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param column The zero-based column where to start.
     * @param row The zero-based line to fill.
     * @param count The number of columns to fill. If this is less than 1,
     *        nothing is done.
     * @param ch The character where to fill the region with.
     *
     * @throws NullPointerException {@code ch} is {@code null}.
     *
     * @see #getClipRect()
     */
    void fill(int column, int row, int count, TextCharacter ch);

    /**
     * Writes {@code count} characters of {@code str} into the given line,
     * starting at {@code column}. The attributes are not changed.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param column The zero-based column where to start.
     * @param row The zero-based line to fill.
     * @param count The number of columns to fill. Is this is greater than the
     *        length of {@code count}, left cells are not changed. If this is
     *        less than the length of {@code str}, the string is truncated. If
     *        this is less than 1, nothing is done.
     * @param str The string to write.
     *
     * @throws NullPointerException {@code str} is {@code null}.
     *
     * @see #getClipRect()
     */
    void write(int column, int row, int count, String str);

    /**
     * Writes {@code count} characters of {@code str} and the attributes into
     * the given line, starting at {@code column}.
     * <p>
     * Each part of the given region that is outside the clipping region, is not
     * filled.
     *
     * @param column The zero-based column where to start.
     * @param row The zero-based line to fill.
     * @param count The number of columns to fill. Is this is greater than the
     *        length of {@code count}, left cells are not changed. If this is
     *        less than the length of {@code str}, the string is truncated. If
     *        this is less than 1, nothing is done.
     * @param str The string to write.
     * @param attr The attributes to use.
     *
     * @throws NullPointerException {@code str} or {@code attr} is {@code null}.
     *
     * @see #getClipRect()
     */
    void write(int column, int row, int count, String str, TextCharacter attr);

    /**
     * Writes the specified character at the specified point into the screen.
     * The attributes are not changed.
     * <p>
     * If the specified point is not within the clipping region, the attributes
     * are not written.
     *
     * @param column The zero-based column where to write the attributes.
     * @param row The zero-based line where to write the attributes.
     * @param ch The character to write.
     *
     * @see #getClipRect()
     */
    void put(int column, int row, char ch);

    /**
     * Writes the specified character at the specified point into the screen.
     * The attributes are not changed.
     * <p>
     * If the specified point is not within the clipping region, the attributes
     * are not written.
     *
     * @param column The zero-based column where to write the attributes.
     * @param row The zero-based line where to write the attributes.
     * @param ch The character to write.
     *
     * @throws NullPointerException {@code ch} is {@code null}.
     *
     * @see #getClipRect()
     */
    void put(int column, int row, TextCharacter ch);
}
