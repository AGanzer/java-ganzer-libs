package de.ganzer.fx.input;

import de.ganzer.fx.internals.FXMessages;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The same as {@link javafx.scene.input.KeyCodeCombination} but with
 * localization.
 */
@SuppressWarnings("unused")
public final class LocalizedKeyCodeCombination extends LocalizedKeyCombination {
    private final KeyCode code;

    /**
     * Gets the key code of this combination.
     *
     * @return The key code.
     */
    public KeyCode getCode() {
        return code;
    }

    /**
     * Constructs a {@code LocalizedKeyCodeCombination} for the specified main
     * key and with an explicit specification of all modifier keys. Each
     * modifier key can be set to {@code PRESSED}, {@code RELEASED} or
     * {@code IGNORED}.
     *
     * @param code the key code of the main key
     * @param shift the value of the {@code shift} modifier key
     * @param control the value of the {@code control} modifier key
     * @param alt the value of the {@code alt} modifier key
     * @param meta the value of the {@code meta} modifier key
     * @param shortcut the value of the {@code shortcut} modifier key
     */
    public LocalizedKeyCodeCombination(KeyCode code,
                                       ModifierValue shift,
                                       ModifierValue control,
                                       ModifierValue alt,
                                       ModifierValue meta,
                                       ModifierValue shortcut) {
        super(shift, control, alt, meta, shortcut);

        validateKeyCode(code);
        this.code = code;
    }

    /**
     * Constructs a {@code LocalizedKeyCodeCombination} for the specified main
     * key and with the specified list of modifiers. All modifier keys which are
     * not explicitly listed are set to the default {@code RELEASED} value.
     * <p>
     * All possible modifiers which change the default modifier value are
     * defined as constants in the {@code KeyCombination} class.
     *
     * @param code the key code of the main key
     * @param modifiers the list of modifier keys and their corresponding values
     */
    public LocalizedKeyCodeCombination(KeyCode code, Modifier... modifiers) {
        super(modifiers);

        validateKeyCode(code);
        this.code = code;
    }


    /**
     * Tests whether this key combination matches the key combination in the
     * given {@code KeyEvent}. It uses only the key code and the state of the
     * modifier keys from the {@code KeyEvent} in the test. This means that the
     * method can return {@code true} only for {@code KEY_PRESSED} and
     * {@code KEY_RELEASED} events, but not for {@code KEY_TYPED} events, which
     * don't have valid key codes.
     *
     * @param event the key event
     * @return {@code true} if the key combinations match, {@code false}
     *         otherwise.
     */
    @Override
    public boolean match(final KeyEvent event) {
        return (event.getCode() == getCode()) && super.match(event);
    }

    /**
     * Returns a localized string representation of this
     * {@code LocalizedKeyCodeCombination}.
     * <p>
     * The string representation consists of sections separated by plus
     * characters. Each section specifies either a modifier key or the main key.
     * <p>
     * A modifier key section contains the {@code KeyCode} name of a modifier
     * key. It can be prefixed with the {@code Ignored} keyword. A non-prefixed
     * modifier key implies its {@code PRESSED} value while the prefixed version
     * implies the {@code IGNORED} value. If some modifier key is not specified
     * in the string at all, it means it has the default {@code RELEASED} value.
     * <p>
     * The main key section contains the key code name of the main key and is
     * the last section in the returned string.
     *
     * @return the string representation of this {@code KeyCodeCombination}
     */
    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.getName());

        if (!sb.isEmpty()) {
            sb.append("+");
        }

        return sb.append(code.getName()).toString();
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDisplayText());

        char c = getSingleChar(code);

        if (c != 0) {
            sb.append(c);
            return sb.toString();
        }

        sb.append(FXMessages.get(FXMessages.get(code.toString())));

        return sb.toString();
    }

    /**
     * Tests whether this {@code KeyCodeCombination} equals to the specified
     * object.
     *
     * @param obj the object to compare to
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof LocalizedKeyCodeCombination)) {
            return false;
        }

        return (this.getCode() == ((LocalizedKeyCodeCombination) obj).getCode()) && super.equals(obj);
    }

    /**
     * Returns a hash code value for this {@code KeyCodeCombination}.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return 23 * super.hashCode() + code.hashCode();
    }

    private static void validateKeyCode(final KeyCode keyCode) {
        if (keyCode == null) {
            throw new NullPointerException("Key code must not be null!");
        }

        if (getModifier(keyCode.getName()) != null) {
            throw new IllegalArgumentException("Key code must not match modifier key!");
        }

        if (keyCode == KeyCode.UNDEFINED) {
            throw new IllegalArgumentException("Key code must differ from undefined value!");
        }
    }

    private static char getSingleChar(KeyCode code) {
        switch (code) {
            case ENTER: return '↵';
            case LEFT: return '←';
            case UP: return '↑';
            case RIGHT: return '→';
            case DOWN: return '↓';
            case COMMA: return ',';
            case MINUS: return '-';
            case PERIOD: return '.';
            case SLASH: return '/';
            case SEMICOLON: return ';';
            case EQUALS: return '=';
            case OPEN_BRACKET: return '[';
            case BACK_SLASH: return '\\';
            case CLOSE_BRACKET: return ']';
            case MULTIPLY: return '*';
            case ADD: return '+';
            case SUBTRACT: return '-';
            case DECIMAL: return '.';
            case DIVIDE: return '/';
            case BACK_QUOTE: return '`';
            case QUOTE: return '"';
            case AMPERSAND: return '&';
            case ASTERISK: return '*';
            case LESS: return '<';
            case GREATER: return '>';
            case BRACELEFT: return '{';
            case BRACERIGHT: return '}';
            case AT: return '@';
            case COLON: return ':';
            case CIRCUMFLEX: return '^';
            case DOLLAR: return '$';
            case EURO_SIGN: return '€';
            case EXCLAMATION_MARK: return '!';
            case LEFT_PARENTHESIS: return '(';
            case NUMBER_SIGN: return '#';
            case PLUS: return '+';
            case RIGHT_PARENTHESIS: return ')';
            case UNDERSCORE: return '_';
            case DIGIT0: return '0';
            case DIGIT1: return '1';
            case DIGIT2: return '2';
            case DIGIT3: return '3';
            case DIGIT4: return '4';
            case DIGIT5: return '5';
            case DIGIT6: return '6';
            case DIGIT7: return '7';
            case DIGIT8: return '8';
            case DIGIT9: return '9';
            default:
                break;
        }

        // On Mac we display these unicode symbols, otherwise we default
        // to the Text version of the char:
        //
        if (System.getProperty("os.name").startsWith("Mac")) {
            switch (code) {
                case BACK_SPACE: return '⌫';
                case ESCAPE: return '⎋';
                case DELETE: return '⌦';
                default:
                    break;
            }
        }

        return 0;
    }
}
