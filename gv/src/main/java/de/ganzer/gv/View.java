package de.ganzer.gv;

import com.googlecode.lanterna.TerminalPosition;
import de.ganzer.gv.core.Position;
import de.ganzer.gv.core.Rectangle;
import de.ganzer.gv.events.*;

import java.util.Objects;

public class View implements Disposable {
    private static final int STATE_IN_ICON = 0x0002;
    private static final int STATE_SELECTED = 0x0004;
    private static final int STATE_FOCUSED = 0x0010;
    private static final int STATE_DRAGGING = 0x0020;
    private static final int STATE_MODAL = 0x0080;
    private static final int STATE_EXPOSED = 0x0100;
    private static final int STATE_BOUNDS_CHECKED = 0x0400;
    private static final int STATE_MAXIMIZING = 0x0800;
    private static final int STATE_MINIMIZING = 0x1000;

    /**
     * Indicates whether the view is visible.
     */
    public static final int STATE_VISIBLE = 0x0001;
    /**
     * Indicates whether the view is minimized.
     */
    public static final int STATE_MINIMIZED = 0x0008;
    /**
     * Indicates whether the view is enabled to interact with the user.
     */
    public static final int STATE_ENABLED = 0x0040;
    /**
     * Indicates whether the view is maximized.
     */
    public static final int STATE_MAXIMIZED = 0x0200;

    /**
     * No option.
     */
    public static final int OPTION_NONE = 0;
    /**
     * The view is selectable and can get the input focus.
     */
    public static final int OPTION_SELECTABLE = 0x000001;
    /**
     * The view wants to get the mouse click that does select the view.
     */
    public static final int OPTION_PROCESS_FIRST_CLICK = 0x000002;
    /**
     * Selecting the view causes the validation of the previously selected view.
     */
    public static final int OPTION_CAUSES_VALIDATION = 0x000004;
    /**
     * The view is shadowed.
     */
    public static final int OPTION_SHADOWED = 0x000008;
    /**
     * The view is horizontally centered.
     */
    public static final int OPTION_X_CENTERED = 0x000010;
    /**
     * The view is vertically centered.
     */
    public static final int OPTION_Y_CENTERED = 0x000020;
    /**
     * The view is horizontally and vertically centered.
     */
    public static final int OPTION_CENTERED = 0x000030;
    /**
     * The view is put in front of all sibling views if it is selected.
     */
    public static final int OPTION_SELECTION_TOPS = 0x000040;
    /**
     * The view can be arranged within a parent that enables arranging children.
     */
    public static final int OPTION_TILEABLE = 0x000080;
    /**
     * The view can be moved by the user.
     */
    public static final int OPTION_MOVEABLE = 0x000100;
    /**
     * The view's size can be changed by the user.
     */
    public static final int OPTION_GROWABLE = 0x000200;
    /**
     * The view can be minimized.
     */
    public static final int OPTION_MINIMIZABLE = 0x000400;
    /**
     * The view is even drawn if the parent is minimized.
     */
    public static final int OPTION_DRAW_IN_ICON = 0x000800;
    /**
     * The view is the top most view, event if it is currently not selected.
     */
    public static final int OPTION_TOPMOST = 0x001000;
    /**
     * The view wants to pre-process keyboard events.
     */
    public static final int OPTION_PREPROCESSING = 0x002000;
    /**
     * The view wants tp post-process keyboard events.
     */
    public static final int OPTION_POSTPROCESSING = 0x004000;
    /**
     * The view forwards pre-process keyboard events to all sub views, even
     * if this view is not the active view.
     */
    public static final int OPTION_FORCE_PREPROCESSING = 0x008000;
    /**
     * The view forwards post process keyboard events to all sub views, even
     * if this view is not the active view.
     */
    public static final int OPTION_FORCE_POSTPROCESSING = 0x0100000;
    /**
     * The view is selected if it is clicked by the mouse.
     */
    public static final int OPTION_FOCUS_ON_CLICK = 0x0200000;

    /**
     * No limit is set.
     */
    public static final int DRAG_LIMIT_NONE = 0;
    /**
     * The view cannot be dragged outside the left edge of the parent group.
     */
    public static final int DRAG_LIMIT_LEFT = 0x01;
    /**
     * The view cannot be dragged outside the right edge of the parent group.
     */
    public static final int DRAG_LIMIT_RIGHT = 0x02;
    /**
     * The view cannot be dragged outside the top edge of the parent group.
     */
    public static final int DRAG_LIMIT_TOP = 0x04;
    /**
     * The view cannot be dragged outside the bottom edge of the parent group.
     */
    public static final int DRAG_LIMIT_BOTTOM = 0x08;
    /**
     * The view cannot be dragged outside any edge of the parent group.
     */
    public static final int DRAG_LIMIT_ALL = 0x0F;

    /**
     * No edge is moved when the parent's size is changed.
     */
    public static final int GROW_NONE = 0;
    /**
     * The left edge is moved when the parent's size is changed.
     */
    public static final int GROW_LEFT = 0x01;
    /**
     * The right edge is moved when the parent's size is changed.
     */
    public static final int GROW_RIGHT = 0x02;
    /**
     * The top edge is moved when the parent's size is changed.
     */
    public static final int GROW_TOP = 0x04;
    /**
     * The bottom edge is moved when the parent's size is changed.
     */
    public static final int GROW_BOTTOM = 0x08;
    /**
     * All edges are moved when the parent's size is changed.
     */
    public static final int GROW_ALL = 0x0F;
    /**
     * The view grows relatively to its parent.
     */
    public static final int GROW_RELATIVELY = 0x10;

    /**
     * The view doesn't want to handle any event.
     */
    public static final int WANTS_NONE = 0;
    /**
     * The view wants to handle the clicks of any mouse button.
     */
    public static final int WANTS_MOUSE_CLICKS = 0x01;
    /**
     * The view wants to handle the double clicks of every mouse button.
     */
    public static final int WANTS_MOUSE_DOUBLE_CLICKS = 0x02;
    /**
     * The view wants to handle the tipple clicks of every mouse button.
     */
    public static final int WANTS_MOUSE_TRIPPLE_CLICKS = 0x04;
    /**
     * The view wants to handle the releases of mouse buttons.
     */
    public static final int WANTS_MOUSE_RELEASES = 0x08;
    /**
     * The view wants to handle mouse moves.
     */
    public static final int WANTS_MOUSE_MOVES = 0x10;
    /**
     * The view wants to handle mouse wheel moving.
     */
    public static final int WANTS_MOUSE_WHEELS = 0x20;
    /**
     * The view wants to handle key down events.
     */
    public static final int WANTS_KEY_DOWN = 0x40;
    /**
     * The view wants to handle key up events.
     */
    public static final int WANTS_KEY_UP = 0x80;
    /**
     * The view wants to handle all mouse events.
     */
    public static final int WANTS_ALL_MOUSE = 0x3F;
    /**
     * The view wants to handle all keyboard events.
     */
    public static final int WANTS_ALL_KEYBOARD = 0xC0;
    /**
     * The view wants to handle all events.
     */
    public static final int WANTS_ALL = 0xFF;

    private final DisposedSupport<View> disposedSupport = new DisposedSupport<>();
    private final PropertyChangedSupport<View> propertyChangedSupport = new PropertyChangedSupport<>();

    private Rectangle bounds;
    private View parent;
    private int status;

    public static class DragResult {
        public final boolean canceled;
        public final TerminalPosition dropPosition;
        public final boolean boundsChanged;

        public DragResult(boolean canceled, TerminalPosition dropPosition, boolean boundsChanged) {
            this.canceled = canceled;
            this.dropPosition = dropPosition;
            this.boundsChanged = boundsChanged;
        }
    }

    /**
     * Gets a value, indicating if the view is visible.
     * <p>
     * The view may not be visible, even if {@code isVisible} returns {@code true}.
     * A view is in fact visible if {@link #isExposed()} is {@code true} and all
     * parent groups are visible and exposed and if the view is not within a
     * minimized group (excepted, {@link #isDrawInIcon()} is {@code true}).
     *
     * @return {@code true} if the view is visible.
     */
    public boolean isVisible() {
        return (status & STATE_VISIBLE) != 0;
    }

    /**
     * Gets a value that indicates whether this view is exposed.
     *
     * @return {@code true} if {@link #isVisible()} of this view and of all
     *         chained parent groups returns {@code true}.
     */
    public boolean isExposed() {
        return (status & STATE_EXPOSED) != 0;
    }

    /**
     * Gets a value, indicating if this view is enabled to interact with the user.
     *
     * @return {@code true} if the view is enabled.
     */
    public boolean isEnabled() {
        return (status & STATE_ENABLED) != 0;
    }

    /**
     * Enables or disables the view.
     * <p>
     * Setting this property to {@code true} may not enable the view in fact.
     * The view is in fact enabled if all parent views are enabled too (see also
     * {@link #canInteract()}).
     * <p>
     * Setting this property does not automatically redraw the view. If the view
     * shall be redrawn, the view must call {@link #draw()} by listening to the
     * "enabled" {@link PropertyChangedEvent} event or by overriding
     * {@link #firePropertyChangedEvent}.
     *
     * @param enabled {@code true} to enable the view.
     */
    public void setEnabled(boolean enabled) {
        if (isEnabled() == enabled)
            return;

        if (enabled)
            status |= STATE_ENABLED;
        else
            status &= ~STATE_ENABLED;

        if (parent != null) {
            if (isFocused())
                parent.focusNext(FocusOrigin.CURRENT, false, false, true);
            else if (isSelectable())
                parent.resetCurrent(false);
        }

        firePropertyChangedEvent(new PropertyChangedEvent<>(this, "enabled"));
    }

    public boolean isSelectable() {
        return false;
    }

    public boolean isFocused() {
        return false;
    }

    public boolean isInIcon() {
        return false;
    }

    public boolean isDrawInIcon() {
        return false;
    }

    public boolean hasShadow() {
        return false;
    }

    /**
     * Gets a value indicating whether the view can interact with the user.
     * <p>
     * A view can interact with the user if the view is enabled and visible and
     * all parent views are enabled and visible too.
     *
     * @return {@code true} if the user is able to interact with this view.
     */
    public boolean canInteract() {
        return parent != null
                ? isEnabled() && isVisible() && parent.canInteract()
                : isEnabled() && isVisible();
    }

    public View getParent() {
        return parent;
    }

    public Rectangle getExtent() {
        return new Rectangle(Position.NULL, bounds.size);
    }

    public Position getPosition() {
        return bounds.getPosition();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        Objects.requireNonNull(bounds, "bounds must not be null.");

        if (this.bounds.equals(bounds))
            return;

        this.bounds = bounds;
        firePropertyChangedEvent(new PropertyChangedEvent<>(null, "bounds"));
    }

    public void insertView(View view) {

    }

    /**
     * Draws this view into the screen.
     *
     * If <see cref="Paint"/> is set to an event handler, the handler must draw
     * 		/// the view content (but not the sub views). If no handler is set, the view
     * 		/// is drawn in <see cref="Draw(IDC)"/>.
     */
    public final void draw() {
        if (!isExposed())
            return;

        if (parent == null)
            drawView();
        else if (hasShadow())
            parent.drawSubViews(bounds.grownBy(ApplicationSettings.getShadowSize()), this, null);
        else
            parent.drawSubViews(bounds, this, null);
    }

    FocusNextResult focusNext(FocusOrigin origin, boolean forward, boolean nested, boolean wrap) {
        return FocusNextResult.NO_SUBVIEW;
    }

    public DragResult dragByMouse(DragMode mode, TerminalPosition position) {
        return new DragResult(true, position, false);
    }

    /**
     * Sets the input focus to the given view.
     * <p>
     * This operation fails if this group is minimized of it may also fail if
     * the given view is not selectable or if the currently focused view cannot
     * release the input focus.
     *
     * @param view The view to focus.
     *
     * @return {@code true} if the view got the input focus.
     *
     * @throws IllegalArgumentException if {@code view} is not a subview of this
     *         view.
     */
    public final boolean focusView(View view) {
        return false;
    }

    @Override
    public void dispose() {
        disposedSupport.fireEvent(new Event<View>(this));
    }

    public void addDisposedListener(DisposedListener<View> listener) {
        disposedSupport.addListener(listener);
    }

    public void removeDisposedListener(DisposedListener<View> listener) {
        disposedSupport.removeListener(listener);
    }

    public void addPropertyChangedListener(PropertyChangedListener<View> listener) {
        propertyChangedSupport.addListener(listener);
    }

    public void removePropertyChangedListener(PropertyChangedListener<View> listener) {
        propertyChangedSupport.removeListener(listener);
    }

    protected void firePropertyChangedEvent(PropertyChangedEvent<View> event) {
        propertyChangedSupport.fireEvent(event);
    }

    /**
     * Selects the most top or the most bottom selectable view.
     *
     * This does not set the input focus if this group is not focused. To set
     * the input focus too, use {@link #focusView}.
     *
     * @param first If this is {@code true}, the most bottom selectable view is
     *        selected. Otherwise, the most top selectable view is selected.
     */
    protected void resetCurrent(boolean first) {

    }

    private void drawView() {

    }

    private void drawSubViews(Rectangle clip, View start, View terminate) {

    }
}
