package de.ganzer.gv.events;

import com.googlecode.lanterna.TerminalPosition;
import de.ganzer.gv.View;

import java.util.Objects;

/**
 * The event that is used when a mouse event occurs.
 */
@Deprecated() // Perhaps this is not required because we create the specialized events later
public class MouseEvent extends Event<View> {
    /**
     * The first button is clicked.
     */
    public static final int BUTTON1_CLICKED = 0x000001;
    /**
     * The second button is clicked.
     */
    public static final int BUTTON2__CLICKED = 0x000002;
    /**
     * The third button is clicked.
     */
    public static final int BUTTON3_CLICKED = 0x000004;
    /**
     * The first button is double-clicked.
     */
    public static final int BUTTON1_DOUBLE_CLICKED = 0x000010;
    /**
     * The second button is double-clicked.
     */
    public static final int BUTTON2_DOUBLE_CLICKED = 0x000020;
    /**
     * The third button is double-clicked.
     */
    public static final int BUTTON3_DOUBLE_CLICKED = 0x000040;
    /**
     * The first button is triple-clicked.
     */
    public static final int BUTTON1_TRIPPLE_CLICKED = 0x00100;
    /**
     * The second button is triple-clicked.
     */
    public static final int BUTTON2_TRIPPLE_CLICKED = 0x00200;
    /**
     * The third button is triple-clicked.
     */
    public static final int BUTTON3_TRIPPLE_CLICKED = 0x00400;
    /**
     * The first button is released.
     */
    public static final int BUTTON1_RELEASED = 0x001000;
    /**
     * The second button is released.
     */
    public static final int BUTTON2_RELEASED = 0x002000;
    /**
     * The third button is released.
     */
    public static final int BUTTON3_RELEASED = 0x004000;
    /**
     * The wheel is moved up.
     */
    public static final int WHEEL_UP = 0x010000;
    /**
     * The wheel is moved down
     */
    public static final int WHEEL_DOWN = 0x020000;
    /**
     * The mouse is moved.
     */
    public static final int MOVED = 0x100000;
    /**
     * Any button is clicked. This is for masking.
     */
    public static final int CLICKED = 0x000007;
    /**
     * Any button is double-clicked. This is for masking.
     */
    public static final int DOUBLE_CLICKED = 0x000070;
    /**
     * Any button is triple-clicked. This is for masking.
     */
    public static final int TRIPPLE_CLICKED = 0x00700;
    /**
     * Any button is released. This is for masking.
     */
    public static final int RELEASED = 0x007000;
    /**
     * The wheel is moved up or down.
     */
    public static final int WHEELED = 0x030000;
    /**
     * Any action of the first button. This is for masking.
     */
    public static final int BUTTON1 = 0x001111;
    /**
     * Any action of the second button. This is for masking.
     */
    public static final int BUTTON2 = 0x002222;
    /**
     * Any action of the third button. This is for masking.
     */
    public static final int BUTTON3 = 0x003333;

    /**
     * The type of the mouse event.
     */
    public final int type;
    /**
     * The position of the mouse.
     */
    public final TerminalPosition position;
    /**
     * {@code true} if the Alt key is down.
     */
    public final boolean altDown;
    /**
     * {@code true} if the Ctrl key is down.
     */
    public final boolean ctrlDown;
    /**
     * {@code true} if the Shift key is down.
     */
    public final boolean shiftDown;

    /**
     * Creates a new instance.
     *
     * @param sender The sender of the event.
     *
     * @throws NullPointerException {@code position} is {@code null}.
     */
    public MouseEvent(View sender, int type, TerminalPosition position, boolean altDown, boolean ctrlDown, boolean shiftDown) {
        super(sender);

        Objects.requireNonNull(position, "position must not be null.");

        this.type = type;
        this.position = position;
        this.altDown = altDown;
        this.ctrlDown = ctrlDown;
        this.shiftDown = shiftDown;
    }
}
