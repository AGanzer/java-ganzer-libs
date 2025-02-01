package de.ganzer.core.util;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

/**
 * A utility class for working with filenames.
 */
@SuppressWarnings("unused")
public final class FileNames {
    /**
     * The default format of a filename that contains a counter (the default
     * is "%1$s (%2$d)") that is used by {@link #getUniqueName(Path, String)}.
     * <p>
     * A counter is used to rename a file for copy operations. Assuming the
     * counter is 2 and the file to copy into the same directory names
     * "file.txt", the default format formats "filename (2).txt"
     */
    public static final String DEFAULT_COUNTED_FORMAT = "%1$s (%2$d)";

    /**
     * The default format of a filename that contains a hint (the default is
     * "%1$s - %2$s") that is used by {@link #getUniqueName(Path, String)}.
     * <p>
     * A hint is used to rename a file for copy operations. Assuming the hint
     * is "Copy" and the file to copy into the same directory names "file.txt",
     * the default format formats "filename - Copy.txt"
     */
    public static final String DEFAULT_HINT_FORMAT = "%1$s - %2$s";

    /**
     * The default format of a filename that contains a hint with a counter
     * (the default is "%1$s - %2$s (%3$d)") that is used by
     * {@link #getUniqueName(Path, String)}.
     * <p>
     * A hint is used to rename a file for copy operations. Assuming the hint
     * is "Copy", the counter is 2 and the file to copy into the same directory
     * names "file.txt", the default format formats "filename - Copy (2).txt"
     */
    public static final String DEFAULT_COUNTED_HINT_FORMAT = "%1$s - %2$s (%3$d)";

    private static final int FOR_LINUX = 0;
    private static final int FOR_MAC = 1;
    private static final int FOR_WINDOWS = 2;
    private static final int FOR_OTHERS = FOR_WINDOWS;
    private static final String[] INVALID_NAME_CHARS = {
            "/",
            "/:",
            "<>\\/\":|*?"};
    private static final String[] INVALID_MASKED_NAME_CHARS = {
            "/",
            ":/",
            "<>\\/\":|"};

    private static String countedFormat = DEFAULT_COUNTED_FORMAT;
    private static String hintFormat = DEFAULT_HINT_FORMAT;
    private static String countedHintFormat = DEFAULT_COUNTED_HINT_FORMAT;
    private static Function<Character, String> charReplacement;

    /**
     * Gets the format string to use for the name together with a sequential
     * number.
     *
     * @return The used format string. This is {@link #DEFAULT_COUNTED_FORMAT}
     *         by default.
     *
     * @see #setCountedFormat(String)
     */
    public static String getCountedFormat() {
        return countedFormat;
    }

    /**
     * Sets the format string to use for the name together with a sequential
     * number.
     * <p>
     * The string must contain at least one "%1$s" and at least one "%2$d".
     * These are the placeholders for the name (1) and for the sequential
     * number (2).
     *
     * @param countedFormat The format string to set. If this is {@code null},
     *        {@link #DEFAULT_COUNTED_FORMAT} is set.
     */
    public static void setCountedFormat(String countedFormat) {
        FileNames.countedFormat = countedFormat == null ? DEFAULT_COUNTED_FORMAT : countedFormat;
    }

    /**
     * Gets the format string to use for the name together with a hint.
     *
     * @return The used format string. This is {@link #DEFAULT_HINT_FORMAT}
     *         by default.
     *
     * @see #setHintFormat(String)
     */
    public static String getHintFormat() {
        return hintFormat;
    }

    /**
     * Sets the format string to use for the name together with a hint.
     * <p>
     * The string must contain at least one "%1$s" and at least one "%2$s".
     * These are the placeholders for the name (1) and for the hint (2).
     *
     * @param hintFormat The format string to set. If this is {@code null},
     *        {@link #DEFAULT_HINT_FORMAT} is set.
     */
    public static void setHintFormat(String hintFormat) {
        FileNames.hintFormat = hintFormat == null ? DEFAULT_HINT_FORMAT : hintFormat;
    }

    /**
     * Gets the format string to use for the name together with a hint and a
     * sequential number.
     *
     * @return The used format string. This is {@link #DEFAULT_COUNTED_HINT_FORMAT}
     *         by default.
     *
     * @see #setCountedHintFormat(String)
     */
    public static String getCountedHintFormat() {
        return countedHintFormat;
    }

    /**
     * Sets the format string to use for the name together with a hint and a
     * sequential number.
     * <p>
     * The string must contain at least one "%1$s", at least one "%2$s" and at
     * least one "%3$d". These are the placeholders for the name (1), for the
     * sequential number (2) and for the hint (3).
     *
     * @param countedHintFormat The format string to set. If this is {@code null},
     *        {@link #DEFAULT_COUNTED_HINT_FORMAT} is set.
     */
    public static void setCountedHintFormat(String countedHintFormat) {
        FileNames.countedHintFormat = countedHintFormat == null ? DEFAULT_COUNTED_HINT_FORMAT : countedHintFormat;
    }

    /**
     * Gets the character replacement function that is used by
     * {@link #getValidName(String)} to replace invalid characters.
     *
     * @return The used function or {@code null} if no function is set.
     *
     * @see #setCharReplacement(Function)
     */
    public static Function<Character, String> getCharReplacement() {
        return charReplacement;
    }

    /**
     * Sets the character replacement function that is used by
     * {@link #getValidName(String)} to replace invalid characters.
     *
     * @param charReplacement The replacement function to set. If this is
     *        {@code null}, invalid characters are replaced with its two digits
     *        Unicode value and a preceding % character.
     */
    public static void setCharReplacement(Function<Character, String> charReplacement) {
        FileNames.charReplacement = charReplacement;
    }

    /**
     * Checks whether the specified name is a valid filename for the current
     * operating system.
     * <p>
     * Note that unter Linux and Mac/OS the jokers ? and * are valid for
     * filenames.
     *
     * @param name The name to check.
     *
     * @return {@code true} if {@code name} is valid; otherwise, {@code false}
     *         is returned.
     *
     * @throws NullPointerException {@code name} is {@code null}.
     *
     * @see #isValidMaskedName(String)
     */
    public static boolean isValidName(String name) {
        return isValidName(name, getInvalidNameChars());
    }

    /**
     * Checks whether the specified name is a valid filename for masking for
     * the current operating system.
     *
     * @param name The name to check. This may contain jokers (? and *).
     *
     * @return {@code true} if {@code name} is valid; otherwise, {@code false}
     *         is returned.
     *
     * @throws NullPointerException {@code name} is {@code null}.
     *
     * @see #isValidName(String)
     */
    public static boolean isValidMaskedName(String name) {
        return isValidName(name, getInvalidMaskedNameChars());
    }

    /**
     * Changes all invalid characters in the given name to build a valid
     * filename for the current operating system.
     * <p>
     * The function that is used by this method is the one that is set at
     * construction or a default function if no other one is set.
     * <p>
     * Note that unter Linux and Mac/OS the jokers ? and * are valid for
     * filenames.
     *
     * @param name The name to make valid.
     *
     * @return {@code name} if {@code name} is valid; otherwise, a valid name
     *         built from {@code name}.
     *
     * @throws NullPointerException {@code name} is {@code null}.
     *
     * @see #getValidMaskedName(String)
     * @see #setCharReplacement(Function)
     */
    public static String getValidName(String name) {
        return getValidName(name, getInvalidNameChars());
    }

    /**
     * Changes all invalid characters in the given name to build a valid
     * filename that may contain jokers for the current operating system.
     * <p>
     * The function that is used by this method is the one that is set at
     * construction or a default function if no other one is set.
     *
     * @param name The name to make valid.
     *
     * @return {@code name} if {@code name} is valid; otherwise, a valid name
     * built from {@code name}. This may contain jokers (? and *).
     *
     * @throws NullPointerException {@code name} is {@code null}.
     *
     * @see #getValidName(String)
     * @see #setCharReplacement(Function)
     */
    public static String getValidMaskedName(String name) {
        return getValidName(name, getInvalidMaskedNameChars());
    }

    /**
     * Creates a unique name from the filename part of the specified path.
     * <p>
     * This method takes the name part of the specified path. This name is
     * modified (without the extensions) as long as another file or directory
     * with that name exists in the directory part of {@code path}. If
     * {@code path} does not contain a directory, the current working directory
     * is used.
     * <p>
     * Note: This method does not validate {@code path}. The resulting path
     * may be invalid if {@code path} is invalid.
     *
     * @param path An absolute or relative path to make unique.
     * @param hint A hint that is included into the resulting name. This may be
     *             a string like "Copy". If this is {@code null} or empty, no
     *             hint is used.
     *
     * @return A path that is unique. This may be equal to {@code path} if
     * {@code path} is already unique and {@code hint} is empty. The only
     * part that may be changed is the name part without extension. {@code null}
     * is returned if no unique name can be created.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getUniqueName(String path, String hint) {
        Objects.requireNonNull(path, "FileNameTools::getUniqueName: path");
        Path result = getUniqueName(Path.of(path), hint);

        return result == null ? null : result.toString();
    }

    /**
     * Creates a unique name from the filename part of the specified path.
     * <p>
     * This method takes the name part of the specified path. This name is
     * modified (without the extensions) as long as another file or directory
     * with that name exists in the directory part of {@code path}. If
     * {@code path} does not contain a directory, the current working directory
     * is used.
     * <p>
     * Note: This method does not validate {@code path}. The resulting path
     * may be invalid if {@code path} is invalid.
     *
     * @param path An absolute or relative path to make unique.
     * @param hint A hint that is included into the resulting name. This may be
     *             a string like "Copy". If this is {@code null} or empty, no
     *             hint is used.
     *
     * @return A path that is unique. This may be equal to {@code path} if
     * {@code path} is already unique and {@code hint} is empty. The only
     * part that may be changed is the name without extension. {@code null}
     * is returned if no unique name can be created.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static Path getUniqueName(Path path, String hint) {
        Objects.requireNonNull(path, "FileNameTools::getUniqueName: path");

        if (!path.toFile().exists())
            return path;

        String dir = path.getParent().toString();
        String name = getNameWithoutExtensions(path);
        String ext = getAllExtensions(path);

        if (!ext.isEmpty())
            ext = '.' + ext;

        boolean hasHint = hint != null && !hint.isEmpty();
        String newName;

        if (hasHint)
            newName = String.format(hintFormat, name, hint);
        else
            newName = String.format(countedFormat, name, 2);

        Path newPath = Path.of(dir, newName + ext);

        if (!newPath.toFile().exists())
            return newPath;

        for (int i = hasHint ? 2 : 3; i < Integer.MAX_VALUE; ++i) {
            if (hasHint)
                newName = String.format(countedHintFormat, name, hint, i);
            else
                newName = String.format(countedFormat, name, i);

            newPath = Path.of(dir, newName + ext);

            if (!newPath.toFile().exists())
                return newPath;
        }

        return null;
    }

    /**
     * Gets the filename part of the specified path without the last extension.
     *
     * @param path The path where to get the filename from.
     *
     * @return The filename without the last extension. This may be empty if
     * the filename in {@code path} starts with a dot and does not contain
     * multiple extensions.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getNameWithoutLastExtension(String path) {
        Objects.requireNonNull(path, "FileNameTools::getNameWithoutLastExtension: path");
        return getNameWithoutLastExtension(Path.of(path));
    }

    /**
     * Gets the filename part of the specified path without the last extension.
     *
     * @param path The path where to get the filename from.
     *
     * @return The filename without the last extension. This may be empty if
     * the filename in {@code path} starts with a dot and does not contain
     * multiple extensions.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getNameWithoutLastExtension(Path path) {
        Objects.requireNonNull(path, "FileNameTools::getNameWithoutLastExtension: path");

        String name = path.getFileName().toString();
        int i = name.lastIndexOf('.');

        if (i == -1)
            return name;

        return name.substring(0, i);
    }

    /**
     * Gets the filename part of the specified path without any extension.
     *
     * @param path The path where to get the filename from.
     *
     * @return The filename without the extensions. This may be empty if
     * the filename in {@code path} starts with a dot.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getNameWithoutExtensions(String path) {
        Objects.requireNonNull(path, "FileNameTools::getNameWithoutExtensions: path");
        return getNameWithoutExtensions(Path.of(path));
    }

    /**
     * Gets the filename part of the specified path without any extension.
     *
     * @param path The path where to get the filename from.
     *
     * @return The filename without the extensions. This may be empty if
     * the filename in {@code path} starts with a dot.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getNameWithoutExtensions(Path path) {
        Objects.requireNonNull(path, "FileNameTools::getNameWithoutExtensions: path");

        String name = path.getFileName().toString();
        int i = name.indexOf('.');

        if (i == -1)
            return name;

        return name.substring(0, i);
    }

    /**
     * Gets the last extension of the filename in the specified path.
     *
     * @param path The path where to get the filename's extension from.
     *
     * @return The last extension without the leading dot of the filename in
     * {@code path}. This may be empty if there is no extension or the
     * filename ends with a dot.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getExtension(String path) {
        Objects.requireNonNull(path, "FileNameTools::getExtension: path");

        int i = path.lastIndexOf('.');

        if (i == -1)
            return "";

        return path.substring(i + 1);
    }

    /**
     * Gets the last extension of the filename in the specified path.
     *
     * @param path The path where to get the filename's extension from.
     *
     * @return The last extension without the leading dot of the filename in
     * {@code path}. This may be empty if there is no extension or the
     * filename ends with a dot.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getExtension(Path path) {
        Objects.requireNonNull(path, "FileNameTools::getExtension: path");
        return getExtension(path.toString());
    }

    /**
     * Gets the all extensions of the filename in the specified path.
     *
     * @param path The path where to get the filename's extension from.
     *
     * @return All extensions without the leading dot of the filename in
     * {@code path}. This may be empty if there is no extension or the
     * filename ends with a single dot.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getAllExtensions(String path) {
        Objects.requireNonNull(path, "FileNameTools::getAllExtensions: path");
        return getAllExtensions(Path.of(path));
    }

    /**
     * Gets the all extensions of the filename in the specified path.
     *
     * @param path The path where to get the filename's extension from.
     *
     * @return All extensions without the leading dot of the filename in
     * {@code path}. This may be empty if there is no extension or the
     * filename ends with a single dot.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    public static String getAllExtensions(Path path) {
        Objects.requireNonNull(path, "FileNameTools::getAllExtensions: path");

        String name = path.getFileName().toString();
        int i = name.indexOf('.');

        if (i == -1)
            return "";

        return name.substring(i + 1);
    }

    private static boolean isValidName(String name, String invalidChars) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (invalidChars.indexOf(c) >= 0)
                return false;
        }

        return true;
    }

    private static String getValidName(String name, String invalidChars) {
        Function<Character, String> replacement = charReplacement != null
                ? charReplacement
                : FileNames::defaultCharReplacement;
        StringBuilder newName = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (invalidChars.indexOf(c) < 0)
                newName.append(c);
            else
                newName.append(replacement.apply(c));
        }

        return newName.toString();
    }

    private static String getInvalidNameChars() {
        return INVALID_NAME_CHARS[getOSSelector()];
    }

    private static String getInvalidMaskedNameChars() {
        return INVALID_MASKED_NAME_CHARS[getOSSelector()];
    }

    private static int getOSSelector() {
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            return FOR_WINDOWS;

        if (System.getProperty("os.name").toLowerCase().contains("linux"))
            return FOR_LINUX;

        if (System.getProperty("os.name").toLowerCase().contains("mac"))
            return FOR_MAC;

        return FOR_OTHERS;
    }

    private static String defaultCharReplacement(Character c) {
        return String.format("%%%02X", (int)c);
    }
}
