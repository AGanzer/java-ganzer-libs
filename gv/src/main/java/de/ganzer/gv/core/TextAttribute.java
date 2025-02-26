package de.ganzer.gv.core;

import com.googlecode.lanterna.TextColor;

/**
 * Defines the attributes of a character in a terminal.
 */
public class TextAttribute {
    private final TextColor foregroundColor;
    private final TextColor backgroundColor;

    /**
     * Creates a new instance from the given argumenta.
     *
     * @param foregroundColor The foreground color to set.
     * @param backgroundColor The background color to set.
     */
    public TextAttribute(TextColor foregroundColor, TextColor backgroundColor) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Gets the foreground color.
     *
     * @return The color set at construction.
     */
    public TextColor getForegroundColor() {
        return foregroundColor;
    }

    /**
     * Gets the background color.
     *
     * @return The color set at construction.
     */
    public TextColor getBackgroundColor() {
        return backgroundColor;
    }
}
