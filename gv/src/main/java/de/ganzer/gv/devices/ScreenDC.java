package de.ganzer.gv.devices;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import de.ganzer.gv.core.Position;
import de.ganzer.gv.core.Rectangle;

import java.io.IOException;
import java.util.Objects;

/**
 * The ScreenDC class defines a DC that enables writing into the whole screen
 * buffer.
 */
public class ScreenDC implements DC {
    private static Screen screen;
    private static int lockCount;
    private static Rectangle clipRegion;

    private Rectangle clipRect;

    /**
     * Creates a new screen wide device context.
     *
     * @throws IllegalStateException If {@link #init} was not called before.
     */
    public ScreenDC() {
        checkInit();
        clipRect = new Rectangle(Position.NULL, screen.getTerminalSize());
    }

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
    @Override
    public final boolean isLocked() {
        return lockCount > 0;
    }

    /**
     * Gets the extent of the device. The left and top edges are always zero.
     *
     * @return The extent of the device.
     *
     * @see #getClipRect()
     *
     * @throws IllegalStateException If {@link #init} was not called before.
     */
    @Override
    public final Rectangle getExtent() {
        checkInit();
        return new Rectangle(Position.NULL, screen.getTerminalSize());
    }

    /**
     * Gets or sets the clipping region.
     *
     * @return The current clipping region.
     */
    @Override
    public final Rectangle getClipRect() {
        return clipRect;
    }

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
     * @throws IllegalStateException If {@link #init} was not called before.
     */
    @Override
    public final void setClipRect(Rectangle clipRect) {
        Objects.requireNonNull(clipRect, "clipRect must not be null.");
        checkInit();

        this.clipRect = clipRect.intersectedBy(clipRegion);
    }

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
     *         otherwise, it is decremented.
     *
     * @return The changed counter value.
     *
     * @throws IllegalStateException If the counter 0 and {@code increment} is
     *         {@code false} or if {@link #init} was not called before.
     */
    @Override
    public final int lock(boolean increment) {
        return lockScreen(increment);
    }

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
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void fill(Rectangle bounds, char ch) {
        checkInit();

        bounds = bounds.intersectedBy(clipRect);

        if (!bounds.isExposed())
            return;

        for (int x = bounds.x; x < bounds.xAndWidth; ++x) {
            for (int y = bounds.y; y < bounds.yAndHeight; ++y) {
                var c = screen.getBackCharacter(x, y);
                screen.setCharacter(x, y, c.withCharacter(ch));
            }
        }

        if (lockCount == 0)
            flush();
    }

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
     *         nothing is done.
     * @param ch The character where to fill the region with.
     *
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void fill(int column, int row, int count, char ch) {
        checkInit();
        fill(new Rectangle(column, row, count, 1), ch);
    }

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
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void fill(Rectangle bounds, TextCharacter ch) {
        Objects.requireNonNull(bounds, "bounds must not be null.");
        Objects.requireNonNull(ch, "ch must not be null.");
        checkInit();

        bounds = bounds.intersectedBy(clipRect);

        if (!bounds.isExposed())
            return;

        for (int x = bounds.x; x < bounds.xAndWidth; ++x) {
            for (int y = bounds.y; y < bounds.yAndHeight; ++y) {
                screen.setCharacter(x, y, ch);
            }
        }

        if (lockCount == 0)
            flush();
    }

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
     *         nothing is done.
     * @param ch The character where to fill the region with.
     *
     * @throws NullPointerException {@code ch} is {@code null}.
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void fill(int column, int row, int count, TextCharacter ch) {
        Objects.requireNonNull(ch, "ch must not be null.");
        checkInit();

        fill(new Rectangle(column, row, count, 1), ch);
    }

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
     *         length of {@code count}, left cells are not changed. If this is
     *         less than the length of {@code str}, the string is truncated. If
     *         this is less than 1, nothing is done.
     * @param str The string to write.
     *
     * @throws NullPointerException {@code str} is {@code null}.
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void write(int column, int row, int count, String str) {
        Objects.requireNonNull(str, "str must not be null.");
        checkInit();

        var bounds = clipRect.intersectedBy(new Rectangle(column, row, count, 1));

        if (!bounds.isExposed())
            return;

        for (int x = bounds.x, i = 0; x < bounds.xAndWidth && i < str.length(); ++x, ++i) {
            for (int y = bounds.y; y < bounds.yAndHeight; ++y) {
                var c = screen.getBackCharacter(x, y);
                screen.setCharacter(x, y, c.withCharacter(str.charAt(i)));
            }
        }

        if (lockCount == 0)
            flush();
    }

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
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void put(int column, int row, char ch) {
        checkInit();

        if (!clipRect.contains(column, row))
            return;

        var c = screen.getBackCharacter(column, row);
        screen.setCharacter(column, row, c.withCharacter(ch));

        if (lockCount == 0)
            flush();
    }

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
     * @throws IllegalStateException If {@link #init} was not called before.
     *
     * @see #getClipRect()
     */
    @Override
    public void put(int column, int row, TextCharacter ch) {
        Objects.requireNonNull(ch, "ch must not be null.");
        checkInit();

        if (!clipRect.contains(column, row))
            return;

        screen.setCharacter(column, row, ch);

        if (lockCount == 0)
            flush();
    }

    /**
     * This forces the DC to write the changed regions of the buffer content
     * into the terminal.
     *
     * @throws IllegalStateException If {@link #init} was not called before.
     */
    public static void flush() {
        checkInit();

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This initializes the terminal and should be called when the application
     * starts.
     *
     * @param terminal The terminal to use. If this is {@code null}, a default
     *        terminal is created.
     */
    public static void init(Terminal terminal) {
        shutDown();

        try {
            if (terminal == null) {
                DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
                terminal = defaultTerminalFactory.createTerminal();
            }

            screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.setCursorPosition(null);

            clipRegion = new Rectangle(Position.NULL, screen.getTerminalSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This shuts the terminal down and should be called when the application is
     * shut down.
     */
    public static void shutDown() {
        try {
            if (screen == null)
                return;

            screen.close();
            screen = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
     *         {@code false} or {@link #init} was not called before.
     */
    private static synchronized int lockScreen(boolean increment) {
        checkInit();

        if (increment) {
            ++lockCount;
        } else {
            if (lockCount == 0)
                throw new IllegalStateException("Screen has already been unlocked");

            --lockCount;
        }

        if (lockCount == 0)
            flush();

        return lockCount;
    }

    private static void checkInit() {
        if (screen == null)
            throw new IllegalStateException("The screen is not initialized.");
    }

    static {
        init(null);
    }
}
