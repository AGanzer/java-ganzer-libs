package de.ganzer.fx.input;

import de.ganzer.fx.internals.FXMessages;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * The same as {@link KeyCombination} but with localization.
 */
@SuppressWarnings("unused")
public abstract class LocalizedKeyCombination extends KeyCombination {
    /**
     * Returns a string representation of this {@code KeyCombination} that is
     * suitable for display in a user interface (for example, beside a menu item).
     *
     * @return A string representation of this {@code KeyCombination}, suitable
     *         for display in a user interface.
     */
    @Override
    public String getDisplayText() {
        if (System.getProperty("os.name").startsWith("Mac"))
            return super.getDisplayText();

        if (this.getControl() == KeyCombination.ModifierValue.DOWN || this.getShortcut() == KeyCombination.ModifierValue.DOWN)
            return FXMessages.get("keyControl") + "+";

        if (this.getAlt() == KeyCombination.ModifierValue.DOWN)
            return FXMessages.get("keyAlt") + "+";

        if (this.getShift() == KeyCombination.ModifierValue.DOWN)
            return FXMessages.get("keyShift") + "+";

        if (this.getMeta() == KeyCombination.ModifierValue.DOWN)
            return FXMessages.get("keyMeta") + "+";

        return "";
    }

    /**
     * Constructs a new {@code KeyCombination} from the specified string. The
     * string should be in the same format as produced by the {@code getName}
     * method.
     * <p>
     * If the main key section string is quoted in single quotes the method
     * creates a new {@code KeyCharacterCombination} for the unquoted substring.
     * Otherwise, it finds the key code which name corresponds to the main key
     * section string and creates a {@code KeyCodeCombination} for it. If this
     * can't be done, it falls back to the {@code KeyCharacterCombination}.
     *
     * @param value the string which represents the requested key combination.
     *
     * @return the constructed {@code KeyCombination}.
     */
    public static KeyCombination valueOf(String value) {
        var combination = KeyCombination.valueOf(value);

        if (combination instanceof KeyCodeCombination)
            return new LocalizedKeyCodeCombination(
                    ((KeyCodeCombination) combination).getCode(),
                    combination.getControl(),
                    combination.getControl(),
                    combination.getAlt(),
                    combination.getMeta(),
                    combination.getShortcut());

        return combination;
    }

    /**
     * Constructs a new {@code KeyCombination} from the specified string. This
     * method simply delegates to {@link #valueOf(String)}.
     *
     * @param name the string which represents the requested key combination.
     *
     * @return the constructed {@code KeyCombination}.
     *
     * @see #valueOf(String)
     */
    public static KeyCombination keyCombination(String name) {
        return valueOf(name);
    }

    /**
     * Constructs a {@code LocalizedKeyCombination} with an explicit
     * specification of all modifier keys. Each modifier key can be set to
     * {@code DOWN}, {@code UP} or {@code ANY}.
     *
     * @param shift the value of the {@code shift} modifier key
     * @param control the value of the {@code control} modifier key
     * @param alt the value of the {@code alt} modifier key
     * @param meta the value of the {@code meta} modifier key
     * @param shortcut the value of the {@code shortcut} modifier key
     */
    protected LocalizedKeyCombination(ModifierValue shift,
                                      ModifierValue control,
                                      ModifierValue alt,
                                      ModifierValue meta,
                                      ModifierValue shortcut) {
        super(shift, control, alt, meta, shortcut);
    }

    /**
     * Constructs a {@code LocalizedKeyCombination} with the specified list of
     * modifiers. All modifier keys which are not explicitly listed are set to
     * the default {@code UP} value.
     * <p>
     * All possible modifiers which change the default modifier value are
     * defined as constants in the {@code KeyCombination} class.
     *
     * @param modifiers the list of modifier keys and their corresponding values
     */
    protected LocalizedKeyCombination(Modifier... modifiers) {
        super(modifiers);
    }

    private static final KeyCombination.Modifier[] POSSIBLE_MODIFIERS = {
            SHIFT_DOWN, SHIFT_ANY,
            CONTROL_DOWN, CONTROL_ANY,
            ALT_DOWN, ALT_ANY,
            META_DOWN, META_ANY,
            SHORTCUT_DOWN, SHORTCUT_ANY
    };

    static KeyCombination.Modifier getModifier(final String name) {
        for (final KeyCombination.Modifier modifier: POSSIBLE_MODIFIERS) {
            if (modifier.toString().equals(name)) {
                return modifier;
            }
        }

        return null;
    }
}
