package de.ganzer.core.files;

/**
 * The FileErrorProvider class provides error information for classes that
 * operates on the file system, like {@link FileCopy} and {@link FileDelete}.
 * <p>
 * This class itself cannot be instantiated. Derived classes indicate errors
 * by calling the protected method {@link #setErrorInfo}.
 */
public abstract class FileErrorProvider {
    /**
     * Describes the errors that may be returned by {@link #getError}.
     */
    public enum Error {
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
         * A directory could not be read.
         */
        READ_DIR,

        /**
         * A directory could not be created.
         */
        CREATE_DIR,

        /**
         * A file or directory is copied into a file.
         */
        TARGET_TYPE,

        /**
         * A source does not exist.
         */
        SOURCE_NOT_EXIST,

        /**
         * The user has canceled an operation.
         */
        CANCELED
    }

    private Error error = Error.NONE;
    private String errorDescription = null;

    /**
     * Gets the code of the occurred error.
     *
     * @return The occurred error or {@link Error#NONE} if no error occurred.
     */
    public Error getError() {
        return error;
    }

    /**
     * Gets the string that describes the occurred error.
     *
     * @return The error description or {@code null} if no error occurred or
     * if no description is available.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Sets the {@link #getError error} to {@link Error#NONE} and the
     * {@link #getErrorDescription description} to {@code null}.
     */
    protected void clearError() {
        error = Error.NONE;
        errorDescription = null;
    }

    /**
     * Sets the specified error information.
     *
     * @param error            The error to set.
     * @param errorDescription The error description to set or {@code null} if
     *                         there is no description available.
     */
    protected void setErrorInfo(Error error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
