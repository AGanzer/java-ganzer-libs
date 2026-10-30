package com.example.uitests.swingdv;

import java.awt.Color;
import java.util.Objects;

/**
 * Utility methods for colors.
 */
public final class Colors {
    /**
     * Returns either {@link Color#BLACK} or {@link Color#WHITE}, whichever
     * provides the better contrast against the given color.
     *
     * @param color The color to get the contrasting color for.
     *
     * @return {@link Color#BLACK} or {@link Color#WHITE}.
     *
     * @throws NullPointerException if {@code color} is {@code null}.
     */
    public static Color getContrastingColor(Color color) {
        Objects.requireNonNull(color, "color must not be null.");
        return getRelativeLuminance(color) > 0.179 ? Color.BLACK : Color.WHITE;
    }

    /**
     * Calculates the WCAG relative luminance of a color.
     *
     * @param color The color to get the relative luminance for.
     *
     * @return The relative luminance in the range 0.0 to 1.0.
     *
     * @throws NullPointerException if {@code color} is {@code null}.
     */
    public static double getRelativeLuminance(Color color) {
        Objects.requireNonNull(color, "color must not be null.");

        double r = linearize(color.getRed() / 255.0);
        double g = linearize(color.getGreen() / 255.0);
        double b = linearize(color.getBlue() / 255.0);

        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    /**
     * Calculates the WCAG contrast ratio between two colors.
     *
     * @param color1 The first color.
     * @param color2 The second color.
     *
     * @return contrast ratio in the range 1.0 to 21.0
     *
     * @throws NullPointerException if {@code color1} or {@code color2} is
     *         {@code null}.
     */
    public static double getContrastRatio(Color color1, Color color2) {
        Objects.requireNonNull(color1, "color1 must not be null.");
        Objects.requireNonNull(color2, "color2 must not be null.");

        double l1 = getRelativeLuminance(color1);
        double l2 = getRelativeLuminance(color2);

        double lighter = Math.max(l1, l2);
        double darker  = Math.min(l1, l2);

        return (lighter + 0.05) / (darker + 0.05);
    }

    /**
     * Indicates whether the given foreground color has at least the given
     * WCAG contrast ratio against the given background.
     *
     * @param foreground The foreground color.
     * @param background The background color.
     * @param minRatio The minimum required contrast ratio.
     *
     * @return {@code true} if the contrast is enough.
     *
     * @throws IllegalArgumentException if {@code minRatio} is less than 1.0.
     */
    public static boolean hasSufficientContrast(Color foreground, Color background, double minRatio) {
        if (minRatio < 1.0)
            throw new IllegalArgumentException("minRatio must be at least 1.0");

        return getContrastRatio(foreground, background) >= minRatio;
    }

    /**
     * Returns the complementary color of the given color.
     *
     * @param color The color to get the complementary color for.
     *
     * @return The complementary color.
     *
     * @throws NullPointerException if {@code color} is {@code null}.
     */
    public static Color getComplementaryColor(Color color) {
        Objects.requireNonNull(color, "color must not be null.");
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    private static double linearize(double component) {
        return component <= 0.04045
                ? component / 12.92
                : Math.pow((component + 0.055) / 1.055, 2.4);
    }

    private Colors() {
    }
}
