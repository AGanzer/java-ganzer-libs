package de.ganzer.gv;

import com.googlecode.lanterna.TerminalPosition;
import de.ganzer.gv.core.Position;
import de.ganzer.gv.core.Rectangle;
import de.ganzer.gv.events.*;

import java.util.Objects;

public class View implements Disposable {
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

    private static final int STATE_NONE = 0;
    private static final int STATE_VISIBLE = 0x0001;
    private static final int STATE_IN_ICON = 0x0002;
    private static final int STATE_SELECTED = 0x0004;
    private static final int STATE_MINIMIZED = 0x0008;
    private static final int STATE_FOCUSED = 0x0010;
    private static final int STATE_DRAGGING = 0x0020;
    private static final int STATE_ENABLED = 0x0040;
    private static final int STATE_MODAL = 0x0080;
    private static final int STATE_EXPOSED = 0x0100;
    private static final int STATE_MAXIMIZED = 0x0200;
    private static final int STATE_BOUNDS_CHECKED = 0x0400;
    private static final int STATE_MAXIMIZING = 0x0800;
    private static final int STATE_MINIMIZING = 0x1000;

    private static final int OPTION_NONE = 0;
    private static final int OPTION_SELECTABLE = 0x000001;
    private static final int OPTION_PROCESS_FIRST_CLICK = 0x000002;
    private static final int OPTION_CAUSES_VALIDATION = 0x000004;
    private static final int OPTION_SHADOWED = 0x000008;
    private static final int OPTION_X_CENTERED = 0x000010;
    private static final int OPTION_Y_CENTERED = 0x000020;
    private static final int OPTION_CENTERED = 0x000030;
    private static final int OPTION_SELECTION_TOPS = 0x000040;
    private static final int OPTION_TILEABLE = 0x000080;
    private static final int OPTION_MOVEABLE = 0x000100;
    private static final int OPTION_GROWABLE = 0x000200;
    private static final int OPTION_MINIMIZABLE = 0x000400;
    private static final int OPTION_DRAW_IN_ICON = 0x000800;
    private static final int OPTION_TOPMOST = 0x001000;
    private static final int OPTION_PREPROCESSING = 0x002000;
    private static final int OPTION_POSTPROCESSING = 0x004000;
    private static final int OPTION_FORCE_PREPROCESSING = 0x008000;
    private static final int OPTION_FORCE_POSTPROCESSING = 0x0100000;
    private static final int OPTION_FOCUS_ON_CLICK = 0x0200000;

    private final DisposedSupport<View> disposedSupport = new DisposedSupport<>();
    private final PropertyChangedSupport<View> propertyChangedSupport = new PropertyChangedSupport<>();

    private Rectangle bounds;
    private View parent;

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

    public boolean isVisible() {
        return false;
    }

    public boolean isExposed() {
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
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "bounds"));
    }

    public DragResult dragByMouse(DragMode mode, MouseEvent event) {
        return new DragResult(true, event.position, false);
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
}
