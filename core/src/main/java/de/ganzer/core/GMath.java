package de.ganzer.core;

import java.util.Objects;

/**
 * This class provides mathematical utility methods.
 */
public class GMath {
    /**
     * Rounds the specified value.
     * <p>
     * This rounds to the nearest value in the following way:
     * <ul>
     *     <li>{@code GMath.round(0.4, 0)} => 0
     *     <li>{@code GMath.round(0.04, 1)} => 0
     *     <li>{@code GMath.round(0.5, 0)} => 1
     *     <li>{@code GMath.round(0.05, 1)} => 0.1
     *     <li>{@code GMath.round(-0.5, 0)} => 0
     *     <li>{@code GMath.round(-0.05, 1)} => 0
     *     <li>{@code GMath.round(-0.6, 0)} => -1
     *     <li>{@code GMath.round(-0.06, 1)} => -0.1
     *     <li>{@code GMath.round(14.0, -1)} => 10
     *     <li>{@code GMath.round(15.0, -1)} => 20
     *     <li>{@code GMath.round(-15.0, -1)} => -10
     *     <li>{@code GMath.round(-16.0, -1)} => -20
     * </ul>
     *
     * @param value The value to round.
     * @param digits The number of digits to round to. If this is negative,
     *               {@code value} is rounded to the specified number of
     *               pre-decimal digits.
     *
     * @return The rounded value.
     */
    public static double round(double value, int digits) {
        double fac = Math.pow(10, digits);
        double res = Math.round(fac * value);

        return res / fac;
    }

    /**
     * Rounds the specified value.
     * <p>
     * This rounds to the nearest value in the following way:
     * <ul>
     *     <li>{@code GMath.round(0.4, 0)} => 0
     *     <li>{@code GMath.round(0.04, 1)} => 0
     *     <li>{@code GMath.round(0.5, 0)} => 1
     *     <li>{@code GMath.round(0.05, 1)} => 0.1
     *     <li>{@code GMath.round(-0.5, 0)} => 0
     *     <li>{@code GMath.round(-0.05, 1)} => 0
     *     <li>{@code GMath.round(-0.6, 0)} => -1
     *     <li>{@code GMath.round(-0.06, 1)} => -0.1
     *     <li>{@code GMath.round(14.0, -1)} => 10
     *     <li>{@code GMath.round(15.0, -1)} => 20
     *     <li>{@code GMath.round(-15.0, -1)} => -10
     *     <li>{@code GMath.round(-16.0, -1)} => -20
     * </ul>
     *
     * @param value The value to round.
     * @param digits The number of digits to round to. If this is negative,
     *               {@code value} is rounded to the specified number of
     *               pre-decimal digits.
     *
     * @return The rounded value.
     */
    public static float round(float value, int digits) {
        float fac = (float)Math.pow(10, digits);
        float res = Math.round(fac * value);

        return res / fac;
    }

    /**
     * Gets the smaller one of the given values.
     *
     * @param a The first value.
     * @param b The second value.
     *
     * @return {@code a} if {@code a} is smaller than {@code b}; otherwise,
     *         {@code b} is returned.
     *
     * @param <T> The type of the values to compare. These must implement
     *        {@link Comparable}.
     */
    public static <T extends Comparable<T>> T min(T a, T b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    /**
     * Gets the greater one of the given values.
     *
     * @param a The first value.
     * @param b The second value.
     *
     * @return {@code a} if {@code a} is greater than {@code b}; otherwise,
     *         {@code b} is returned.
     *
     * @param <T> The type of the values to compare. These must implement
     *        {@link Comparable}.
     */
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * Returns the value that is in the range [{@code min}, {@code max}].
     *
     * @param value The value to adjust.
     * @param min The minimum allowed value to return.
     * @param max The maximum allowed value to return.
     *
     * @return The value that is in the range [{@code min}, {@code max}].
     */
    public static int toRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns the value that is in the range [{@code min}, {@code max}].
     *
     * @param value The value to adjust.
     * @param min The minimum allowed value to return.
     * @param max The maximum allowed value to return.
     *
     * @return The value that is in the range [{@code min}, {@code max}].
     */
    public static long toRange(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns the value that is in the range [{@code min}, {@code max}].
     *
     * @param value The value to adjust.
     * @param min The minimum allowed value to return.
     * @param max The maximum allowed value to return.
     *
     * @return The value that is in the range [{@code min}, {@code max}].
     */
    public static float toRange(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns the value that is in the range [{@code min}, {@code max}].
     *
     * @param value The value to adjust.
     * @param min The minimum allowed value to return.
     * @param max The maximum allowed value to return.
     *
     * @return The value that is in the range [{@code min}, {@code max}].
     */
    public static double toRange(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns the value that is in the range [{@code min}, {@code max}].
     *
     * @param value The value to adjust.
     * @param min The minimum allowed value to return.
     * @param max The maximum allowed value to return.
     *
     * @return The value that is in the range [{@code min}, {@code max}].
     *
     * @param <T> The type of the values to compare. These must implement
     *        {@link Comparable}.
     *
     * @throws NullPointerException {@code value}, {@code min} or {@code max}
     *         is {@code null}.
     */
    public static <T extends Comparable<T>> T toRange(T value, T min, T max) {
        return GMath.min(GMath.max(value, min), max);
    }

    /**
     * Determines whether the specified value is in the range
     * [{@code min}, {@code max}].
     *
     * @param value The value to check.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     *
     * @return {@code true} if {@code value} is not less than {@code min} and
     * not greater than {@code max}; otherwise, {@code false} is returned.
     */
    public static boolean isInRange(int value, int min, int max) {
        return min <= value && value <= max;
    }

    /**
     * Determines whether the specified value is in the range
     * [{@code min}, {@code max}].
     *
     * @param value The value to check.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     *
     * @return {@code true} if {@code value} is not less than {@code min} and
     * not greater than {@code max}; otherwise, {@code false} is returned.
     */
    public static boolean isInRange(long value, long min, long max) {
        return min <= value && value <= max;
    }

    /**
     * Determines whether the specified value is in the range
     * [{@code min}, {@code max}].
     *
     * @param value The value to check.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     *
     * @return {@code true} if {@code value} is not less than {@code min} and
     * not greater than {@code max}; otherwise, {@code false} is returned.
     */
    public static boolean isInRange(float value, float min, float max) {
        return min <= value && value <= max;
    }

    /**
     * Determines whether the specified value is in the range
     * [{@code min}, {@code max}].
     *
     * @param value The value to check.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     *
     * @return {@code true} if {@code value} is not less than {@code min} and
     * not greater than {@code max}; otherwise, {@code false} is returned.
     */
    public static boolean isInRange(double value, double min, double max) {
        return min <= value && value <= max;
    }

    /**
     * Determines whether the specified value is in the range
     * [{@code min}, {@code max}].
     *
     * @param value The value to check.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     *
     * @return {@code true} if {@code value} is not less than {@code min} and
     * not greater than {@code max}; otherwise, {@code false} is returned.
     *
     * @param <T> The type of the values to compare. These must implement
     *        {@link Comparable}.
     *
     * @throws NullPointerException {@code value}, {@code min} or {@code max}
     *         is {@code null}.
     */
    public static <T extends Comparable<T>> boolean isInRange(T value, T min, T max) {
        Objects.requireNonNull(value, "value must not be null.");
        Objects.requireNonNull(min, "min must not be null.");
        Objects.requireNonNull(max, "max must not be null.");

        return min.compareTo(value) <= 0 && value.compareTo(max) <= 0;
    }
}
