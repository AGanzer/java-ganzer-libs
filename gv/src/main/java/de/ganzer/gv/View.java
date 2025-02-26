package de.ganzer.gv;

import de.ganzer.gv.core.Position;
import de.ganzer.gv.core.Rectangle;
import de.ganzer.gv.events.DisposedListener;

import java.util.Objects;

public class View implements Disposable {
    private Rectangle bounds;
    private View parent;

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

    public Rectangle getBounds() {
        return bounds;
    }

    public Position getPosition() {
        return bounds.getPosition();
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void addDisposedListener(DisposedListener<View> listener) {
        Objects.requireNonNull(listener, "listener must not be null.");
    }

    public void removeDisposedListener(DisposedListener<View> listener) {
        Objects.requireNonNull(listener, "listener must not be null.");
    }

    @Override
    public void dispose() {

    }
}
