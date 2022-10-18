package de.ganzer.core.files;

/**
 * Defines the possible behaviors of {@link FileCopy} on existing files
 * and directories.
 */
public enum OverwriteAction {
    /**
     * The currently queried file or directory is not overwritten. The next
     * one is queried again.
     */
    NOT,

    /**
     * No file or directory is overwritten. No further query will occur.
     */
    NONE,

    /**
     * The currently queried file or directory is overwritten. The next one is
     * queried again.
     */
    ONE,

    /**
     * All files or directories are overwritten. No further query will occur.
     */
    ALL,

    /**
     * Cancels copying. {@link FileErrorProvider#getError()} will return
     * {@link FileError#CANCELED} after finishing.
     */
    CANCEL
}
