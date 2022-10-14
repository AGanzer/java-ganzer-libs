package de.ganzer.core.files;

/**
 * Defines the possible reaction to perform at an occurred error.
 */
public enum ErrorAction {
    /**
     * Copying is aborted completely.
     */
    ABORT,

    /**
     * Copying the file or directory that caused the error is retried.
     */
    RETRY,

    /**
     * The entry that caused the error is ignored and not copied.
     */
    IGNORE,

    /**
     * The entry that caused the error is ignored and not copied as well
     * as all further entries that will cause the same error.
     */
    IGNORE_ALL_THIS,

    /**
     * The entry that caused the error is ignored and not copied as well
     * as all further entries that will cause any error.
     */
    IGNORE_ALL
}
