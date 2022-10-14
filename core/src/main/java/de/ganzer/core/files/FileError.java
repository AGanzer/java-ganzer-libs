package de.ganzer.core.files;

/**
 * Describes the errors that may be returned by {@link FileErrorProvider#getError}.
 */
public enum FileError {
    /**
     * No error occurred.
     */
    NONE,

    /**
     * Access was denied.
     */
    ACCESS,

    /**
     * A file could not be read.
     */
    READ_FILE,

    /**
     * A file could not be written.
     */
    WRITE_FILE,

    /**
     * A file could not be created.
     */
    CREATE_FILE,

    /**
     * A file cannot be renamed.
     */
    RENAME_FILE,

    /**
     * A file cannot be removed.
     */
    DELETE_FILE,

    /**
     * A directory could not be read.
     */
    READ_DIR,

    /**
     * A directory could not be created.
     */
    CREATE_DIR,

    /**
     * A directory cannot be renamed.
     */
    DELETE_DIR,

    /**
     * The target ist not a directory.
     */
    TARGET_TYPE,

    /**
     * A directory is copied into a file or vice versa.
     */
    TARGET_SOURCE_TYPE,

    /**
     * A source does not exist.
     */
    SOURCE_NOT_EXIST,

    /**
     * Permissions or times cannt be set.
     */
    SET_ATTRIBUTES,

    /**
     * The user has canceled an operation.
     */
    CANCELED
}
