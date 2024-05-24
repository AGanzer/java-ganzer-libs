package de.ganzer.core.files;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

/**
 * A utility class for working with filenames.
 */
@SuppressWarnings("unused")
public class FileNames {
    private static final int FOR_LINUX = 0;
    private static final int FOR_MAC = 1;
    private static final int FOR_WINDOWS = 2;
    private static final int FOR_OTHERS = FOR_WINDOWS;
    private static final String[] INVALID_NAME_CHARS = {
            "/",
            "/:",
            "<>\\/\":|*?"
    };
    private static final String[] INVALID_MASKED_NAME_CHARS = {
            "/",
            ":/",
            "<>\\/\":|"
    };

    private final String countedFormat;
    private final String hintFormat;
    private final String countedHintFormat;
    private final Function<Character, String> charReplacement;

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
    private String path;

    /**
     * Creates a new instance with default format settings and a default
     * character replacement.
     *
     * @see #DEFAULT_COUNTED_FORMAT
     * @see #DEFAULT_COUNTED_HINT_FORMAT
     * @see #DEFAULT_HINT_FORMAT
     * @see #getValidName(Path)
     * @see #getUniqueName(Path, String)
     */
    public FileNames() {
        this(DEFAULT_COUNTED_FORMAT, DEFAULT_HINT_FORMAT, DEFAULT_COUNTED_HINT_FORMAT, null);
    }

    /**
     * Creates a new instance from the specified arguments and a default
     * character replacement.
     *
     * @param countedFormat The format to sue for counted names.
     * @param hintFormat The format to use for names with a hint.
     * @param countedHintFormat The format to use for names with a counted hint.
     *
     * @see #getValidName(Path)
     * @see #getUniqueName(Path, String)
     *
     * @throws NullPointerException {@code countedFormat}, {@code hintFormat} or
     * {@code countedHintFormat} is {@code null}.
     */
    public FileNames(String countedFormat, String hintFormat, String countedHintFormat) {
        this(countedFormat, hintFormat, countedHintFormat, null);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param countedFormat The format to sue for counted names.
     * @param hintFormat The format to use for names with a hint.
     * @param countedHintFormat The format to use for names with a counted hint.
     * @param charReplacement The character replacement function that is used by
     *                        {@link #getValidName(String)} to replace invalid
     *                        characters. If this is {@code null}, the default
     *                        replacement is used to replace invalid characters
     *                        with its two digits Unicode value and a preceding
     *                        % character.
     *
     * @see #DEFAULT_COUNTED_FORMAT
     * @see #DEFAULT_COUNTED_HINT_FORMAT
     * @see #DEFAULT_HINT_FORMAT
     * @see #getValidName(Path)
     * @see #getUniqueName(Path, String)
     *
     * @throws NullPointerException {@code countedFormat}, {@code hintFormat} or
     * {@code countedHintFormat} is {@code null}.
     */
    public FileNames(String countedFormat, String hintFormat, String countedHintFormat, Function<Character, String> charReplacement) {
        Objects.requireNonNull(countedFormat);
        Objects.requireNonNull(hintFormat);
        Objects.requireNonNull(countedHintFormat);

        this.countedFormat = countedFormat;
        this.hintFormat = hintFormat;
        this.countedHintFormat = countedHintFormat;
        this.charReplacement = charReplacement;
    }

    /**
     * Checks whether the name part of the specified path is a valid filename
     * for the current operating system.
     * <p>
     * Note that unter Linux and Mac/OS the jokers ? and * are valid for
     * filenames.
     *
     * @param path The path to check the name from.
     *
     * @return {@code true} if the name part of {@code path} is valid;
     * otherwise, {@code false} is returned.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #isValidMaskedName(String)
     */
    public boolean isValidName(String path) {
        return isValidName(Path.of(path, getInvalidNameChars()));
    }

    /**
     * Checks whether the name part of the specified path is a valid filename
     * for the current operating system.
     * <p>
     * Note that unter Linux and Mac/OS the jokers ? and * are valid for
     * filenames.
     *
     * @param path The path to check the name from.
     *
     * @return {@code true} if the name part of {@code path} is valid;
     * otherwise, {@code false} is returned.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #isValidMaskedName(Path)
     */
    public boolean isValidName(Path path) {
        Objects.requireNonNull(path);
        return isValidName(path, getInvalidNameChars());
    }

    /**
     * Checks whether the name part of the specified path is a valid filename
     * for masking for the current operating system.
     *
     * @param path The path to check the name from. This may contain jokers
     *             (? and *).
     *
     * @return {@code true} if the name part of {@code path} is valid;
     * otherwise, {@code false} is returned.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #isValidName(String)
     */
    public boolean isValidMaskedName(String path) {
        return isValidName(Path.of(path, getInvalidMaskedNameChars()));
    }

    /**
     * Checks whether the name part of the specified path is a valid filename
     * for masking for the current operating system.
     *
     * @param path The path to check the name from. This may contain jokers
     *             (? and *).
     *
     * @return {@code true} if the name part of {@code path} is valid;
     * otherwise, {@code false} is returned.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #isValidName(Path)
     */
    public boolean isValidMaskedName(Path path) {
        Objects.requireNonNull(path);
        return isValidName(path, getInvalidMaskedNameChars());
    }

    /**
     * Changes all invalid characters of the filename part of the given path
     * to build a valid filename for the current operating system.
     * <p>
     * The function that is used by this method is the one that is set at
     * construction or a default function if no other one is set.
     * <p>
     * Note that unter Linux and Mac/OS the jokers ? and * are valid for
     * filenames.
     *
     * @param path The path where to validate the filename part.
     *
     * @return {@code path} with a valid filename.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #getValidMaskedName(String)
     */
    public String getValidName(String path) {
        return getValidName(Path.of(path), getInvalidNameChars()).toString();
    }

    /**
     * Changes all invalid characters of the filename part of the given path
     * to build a valid filename for the current operating system.
     * <p>
     * The function that is used by this method is the one that is set at
     * construction or a default function if no other one is set.
     * <p>
     * Note that unter Linux and Mac/OS the jokers ? and * are valid for
     * filenames.
     *
     * @param path The path where to validate the filename part.
     *
     * @return {@code path} with a valid filename.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #getValidMaskedName(Path)
     */
    public Path getValidName(Path path) {
        Objects.requireNonNull(path);
        return getValidName(path, getInvalidNameChars());
    }

    /**
     * Changes all invalid characters of the filename part of the given path
     * to build a valid filename for the current operating system.
     * <p>
     * The function that is used by this method is the one that is set at
     * construction or a default function if no other one is set.
     * <p>
     *
     * @param path The path where to validate the filename part.
     *
     * @return {@code path} with a valid filename. This may contain jokers
     * (? and *).
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #getValidName(String)
     * @see #FileNames {@literal (String, String, String, Function<Character, String>)}
     */
    public String getValidMaskedName(String path) {
        return getValidName(Path.of(path), getInvalidMaskedNameChars()).toString();
    }

    /**
     * Changes all invalid characters of the filename part of the given path
     * to build a valid filename for the current operating system.
     * <p>
     * The function that is used by this method is the one that is set at
     * construction or a default function if no other one is set.
     * <p>
     *
     * @param path The path where to validate the filename part.
     *
     * @return {@code path} with a valid filename. This may contain jokers
     * (? and *).
     *
     * @throws NullPointerException {@code path} is {@code null}.
     *
     * @see #getValidName(Path)
     * @see #FileNames {@literal (String, String, String, Function<Character, String>)}
     */
    public Path getValidMaskedName(Path path) {
        Objects.requireNonNull(path);
        return getValidName(path, getInvalidMaskedNameChars());
    }

    /**
     * Creates a unique name from the filename part of the specified path.
     * <p>
     * This method takes the name part of the specified path. This name is
     * modified (without the extension) as long as another file or directory
     * with that name exists in the directory part of {@code path}. If
     * {@code path} does not contain a directory, the current working directory
     * is used.
     * <p>
     * Note: This method does not validate {@code path}. The resulting path
     * may be invalid if {@code path} is invalid.
     *
     * @param path An absolute or relative path to make unique.
     * @param hint A hint that is included into the resulting name. This may be
     *             a string like "Copy". If this is {@code null} or empty, not
     *             hint is used.
     *
     * @return A name that is unique. This may be equal to {@code path if
     * {@code path} is already unique and {@code hint} is empty. The only
     * part that may be changed is the name without extension. {@code null}
     * is returned if {@code path} is empty or if no unique name can be
     * created.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    @SuppressWarnings("JavadocBlankLines")
    public String getUniqueName(String path, String hint) {
        return getUniqueName(Path.of(path), hint).toString();
    }

    /**
     * Creates a unique name from the filename part of the specified path.
     * <p>
     * This method takes the name part of the specified path. This name is
     * modified (without the extension) as long as another file or directory
     * with that name exists in the directory part of {@code path}. If
     * {@code path} does not contain a directory, the current working directory
     * is used.
     * <p>
     * Note: This method does not validate {@code path}. The resulting path
     * may be invalid if {@code path} is invalid.
     *
     * @param path An absolute or relative path to make unique.
     * @param hint A hint that is included into the resulting name. This may be
     *             a string like "Copy". If this is {@code null} or empty, not
     *             hint is used.
     *
     * @return A name that is unique. This may be equal to {@code path if
     * {@code path} is already unique and {@code hint} is empty. The only
     * part that may be changed is the name without extension. {@code null}
     * is returned if {@code path} is empty or if no unique name can be
     * created.
     *
     * @throws NullPointerException {@code path} is {@code null}.
     */
    @SuppressWarnings("JavadocBlankLines")
    public Path getUniqueName(Path path, String hint) {
        if (!path.toFile().exists())
            return path;

        String dir = path.getParent().toString();
        String name = getNameWithoutExtensions(path);
        String ext = getExtension(name);

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
                newName = String.format(countedHintFormat, name, hint);
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
    public String getNameWithoutLastExtension(String path) {
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
    public String getNameWithoutLastExtension(Path path) {
        Objects.requireNonNull(path);

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
    public String getNameWithoutExtensions(String path) {
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
    public String getNameWithoutExtensions(Path path) {
        Objects.requireNonNull(path);

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
    public String getExtension(String path) {
        Objects.requireNonNull(path);

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
    public String getExtension(Path path) {
        Objects.requireNonNull(path);
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
    public String getAllExtensions(String path) {
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
    public String getAllExtensions(Path path) {
        Objects.requireNonNull(path);

        String name = path.getFileName().toString();
        int i = name.indexOf('.');

        if (i == -1)
            return "";

        return name.substring(i + 1);
    }

    private boolean isValidName(Path path, String invalidChars) {
        String name = path.getFileName().toString();

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (invalidChars.indexOf(c) < 0)
                return false;
        }

        return true;
    }

    private Path getValidName(Path path, String invalidChars) {
        Function<Character, String> replacement = charReplacement != null
                ? charReplacement
                : FileNames::defaultCharReplacement;
        String name = path.getFileName().toString();
        StringBuilder newName = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (invalidChars.indexOf(c) < 0)
                newName.append(c);
            else
                newName.append(replacement.apply(c));
        }

        return Path.of(path.getParent().toString(), newName.toString());
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
