package de.ganzer.core.files;

/**
 * Defines the possible reaction to perform at an occurred error.
 */
public enum DeleteErrorAction {
    /**
     * Deleting is aborted completely.
     */
    ABORT,

    /**
     * Deleting the file or directory that caused the error is retried.
     */
    RETRY,

    /**
     * The entry that caused the error is ignored and not deleted. This may lead
     * to further errors because not empty directories cannot be deleted.
     */
    IGNORE,

    /**
     * The entry that caused the error is ignored and not copied as well
     * as all further entries that will cause the same error.
     *
     * @since 6.3.0
     */
    IGNORE_ALL_THIS,

    /**
     * The entry that caused the error is ignored and not copied as well
     * as all further entries that will cause any error.
     *
     * @since 6.3.0
     */
    IGNORE_ALL
}
