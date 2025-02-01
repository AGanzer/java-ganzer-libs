package de.ganzer.core.util;

/**
 * Utility class for working with strings.
 */
public final class Strings {
    /**
     * Gets a value that indicates whether the specified string is {@code null}
     * or empty.
     *
     * @param str The string to query.
     *
     * @return {@code true} if {@code str} is {@code null} or empty.
     */
    public static boolean isNullOrEmpty(final String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Gets a value that indicates whether the specified string is {@code null}
     * or empty or contains only whitespaces.
     *
     * @param str The string to query.
     *
     * @return {@code true} if {@code str} is {@code null} or empty or contains
     *         only whitespaces.
     */
    public static boolean isNullOrBlank(final String str) {
        return str == null || str.isBlank();
    }
}
