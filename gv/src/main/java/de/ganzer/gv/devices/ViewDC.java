package de.ganzer.gv.devices;

import com.googlecode.lanterna.TextCharacter;
import de.ganzer.gv.ApplicationSettings;
import de.ganzer.gv.View;
import de.ganzer.gv.core.Position;
import de.ganzer.gv.core.Rectangle;

import java.util.Objects;

/**
 * The ViewDC class encapsulates a DC that enables drawing into a view.
 */
public class ViewDC extends ScreenDC {
    private View view;
    private Rectangle viewClipRect;
    private Rectangle shadowedClipRect;
    private Position delta;

    /**
     * Creates a new instance that draws into the given view.
     *
     * @param view The view where to write into.
     */
    public ViewDC(View view) {
        Objects.requireNonNull(view, "view must not be null.");

        this.view = view;
        this.view.addDisposedListener(e -> this.view = null);
        shadowedClipRect = Rectangle.EMPTY;

        if (view.isVisible() && view.isExposed() && (!view.isInIcon() || view.isDrawInIcon())) {
            shadowedClipRect = view.getBounds();
            delta = view.getPosition();

            if (view.hasShadow()) {
                shadowedClipRect = shadowedClipRect.grownBy(
                        ApplicationSettings.getShadowSize().getColumns(),
                        ApplicationSettings.getShadowSize().getRows());
            }

            for (View parent = view.getParent(); parent != null; parent = parent.getParent()) {
                shadowedClipRect = shadowedClipRect.movedBy(parent.getPosition().getColumn(), parent.getPosition().getRow());
                shadowedClipRect = shadowedClipRect.intersectedBy(parent.getBounds());

                delta.movedBy(parent.getPosition().getColumn(), parent.getPosition().getRow());

                if (!shadowedClipRect.isExposed())
                    break;
            }

            viewClipRect = view.getExtent()
                    .movedBy(delta.getColumn(), delta.getRow())
                    .intersectedBy(shadowedClipRect);
        }
    }

    /**
     * Gets the extent of the device. The left and top edges are always zero.
     *
     * @return The extent of the device.
     *
     * @throws IllegalStateException If {@link #init} was not called before or
     *         the view is already disposed.
     *
     * @see #getClipRect()
     */
    @Override
    public Rectangle getExtent() {
        verifyInit();
        verifyViewState();

        return view.getExtent();
    }

    /**
     * Gets or sets the clipping region.
     *
     * @return The current clipping region.
     */
    @Override
    public Rectangle getClipRect() {
        return super.getClipRect().movedBy(-delta.getColumn(), -delta.getRow());
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
    public void setClipRect(Rectangle clipRect) {
        super.setClipRect(clipRect.movedBy(delta.getColumn(), delta.getRow()).intersectedBy(viewClipRect));
    }

    /**
     * Draws a shadow around the view.
     *
     * @throws IllegalStateException If {@link #init} was not called before or
     *         the view is already disposed.
     */
    public void drawShadow() {
        verifyInit();

        super.setClipRect(shadowedClipRect);

        try {
            super.fillChar(new Rectangle(view.getBounds().width,
                                         ApplicationSettings.getShadowSize().getRows(),
                                         ApplicationSettings.getShadowSize().getColumns(),
                                         view.getBounds().height - ApplicationSettings.getShadowSize().getRows())
                               .movedBy(delta.getColumn(), delta.getRow()),
                           ' '); // TODO: Hier doch Attribute einfügen!!!
            super.fillChar(new Rectangle(ApplicationSettings.getShadowSize().getColumns(),
                                         view.getBounds().height,
                                         view.getBounds().width,
                                         ApplicationSettings.getShadowSize().getRows())
                               .movedBy(delta.getColumn(), delta.getRow()),
                           ' '); // TODO: Hier doch Attribute einfügen!!!
        } finally {
            super.setClipRect(viewClipRect);
        }
    }

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
     * @see #getClipRect()
     */
    @Override
    public void fillAttr(Rectangle bounds, TextCharacter attr) {
        super.fillAttr(bounds, attr);
    }

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
     *         nothing is done.
     * @param attr The attributes where to fill the region with.
     *
     * @throws NullPointerException {@code ch} is {@code null}.
     * @see #getClipRect()
     */
    @Override
    public void fillAttr(int column, int row, int count, TextCharacter attr) {
        super.fillAttr(column, row, count, attr);
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
    public void fillChar(Rectangle bounds, char ch) {
        super.fillChar(bounds, ch);
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
    public void fillChar(int column, int row, int count, char ch) {
        super.fillChar(column, row, count, ch);
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
        super.fill(bounds, ch);
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
        super.fill(column, row, count, ch);
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
        super.write(column, row, count, str);
    }

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
     *         length of {@code count}, left cells are not changed. If this is
     *         less than the length of {@code str}, the string is truncated. If
     *         this is less than 1, nothing is done.
     * @param str The string to write.
     * @param attr The attributes to use.
     *
     * @throws NullPointerException {@code str} or {@code attr} is {@code null}.
     * @see #getClipRect()
     */
    @Override
    public void write(int column, int row, int count, String str, TextCharacter attr) {
        super.write(column, row, count, str, attr);
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
        super.put(column, row, ch);
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
        super.put(column, row, ch);
    }

    private void verifyViewState() {
        if (view == null)
            throw new IllegalStateException("The view is already disposed.");
    }
}
