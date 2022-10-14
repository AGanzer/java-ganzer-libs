package de.ganzer.core.files;

/**
 * Defines the status of the progress when copying files with {@link FileCopy}
 * or {@link FileDelete}.
 */
public enum ProgressStatus {
    /**
     * The copying operation is initialized and the total bytes to copy are
     * counted. The progress function is called once at startup with 0 counted
     * bytes and later for each directory and file that is worked.
     * <p>
     * This status is never reported if the initialization is suppressed via
     * {@link FileCopy#start}.
     */
    INITIALIZING,

    /**
     * Reports that a directory is going to be copied. The progress function is
     * called once for each directory.
     */
    START_DIRECTORY,

    /**
     * Reports that a directory is fully worked. The progress function is called
     * once for each directory after the directory is copied.
     */
    FINISHED_DIRECTORY,

    /**
     * Reports that a file is going to be copied. The progress function is
     * called once for each file.
     */
    START_FILE,

    /**
     * Reports that a file is copied completely. The progress function is
     * called once for each file after the file is copied.
     */
    FINISHED_FILE,

    /**
     * Reports the progress of a file that is currently copied. The progress
     * function may be called multiple times until the whole file is copied.
     */
    COPYING_FILE,

    /**
     * This status reports that all files are copied or that the operation is
     * aborted either by the user or by an error.
     */
    FINISHED
}
