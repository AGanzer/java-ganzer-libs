package de.ganzer.core.files;

/**
 * Defines the status of the progress when deleting files with {@link FileDelete}.
 */
public enum DeleteProgressStatus {
    /**
     * The delete operation is initialized and the total entries to delete are
     * counted. The progress function is called once at startup with 0 counted
     * entries and later for each directory and file that is worked.
     * <p>
     * This status is never reported if the initialization is suppressed via
     * {@link FileDelete#start}.
     */
    INITIALIZING,

    /**
     * Reports that a directory is going to be deleted. The progress function is
     * called once for each directory.
     */
    DELETE_DIRECTORY,

    /**
     * Reports that a file is going to be deleted. The progress function is
     * called once for each file.
     */
    DELETE_FILE,

    /**
     * This status reports that all files are deleted or that the operation is
     * aborted either by the user or by an error.
     */
    FINISHED
}
