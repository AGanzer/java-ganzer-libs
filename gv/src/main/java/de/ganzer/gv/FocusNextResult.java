package de.ganzer.gv;

/**
 * The FocusNextResult enumeration defines the possible results of a call
 * to {@link View#focusNext}.
 */
public enum FocusNextResult {
    /**
     * The next view cannot be focused because the current view cannot be
     * unfocused.
     */
    FAILURE,
    /**
     * The next view cannot be focused because there is no further view
     * available.
     */
    NO_SUBVIEW,
    /**
     * The next view got the input focus successfully.
     */
    SUCCESS
}
