package de.ganzer.gv.events;

import com.googlecode.lanterna.input.KeyType;
import de.ganzer.gv.View;

public class KeyEvent extends Event<View> {
    /**
     * The type of the key.
     */
    public final KeyType keyType;
    /**
     * The character that is represented by the key (this may be 0).
     */
    public final char character;
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
     * @param keyType The key type.
     * @param character The character.
     * @param altDown {@code true} to indicate that the Alt key is down.
     * @param ctrlDown  {@code true} to indicate that the Ctrl key is down.
     * @param shiftDown  {@code true} to indicate that the Shift key is down.
     */
    public KeyEvent(View sender, KeyType keyType, char character, boolean altDown, boolean ctrlDown, boolean shiftDown) {
        super(sender);
        this.keyType = keyType;
        this.character = character;
        this.altDown = altDown;
        this.ctrlDown = ctrlDown;
        this.shiftDown = shiftDown;
    }
}
