package de.ganzer.core.files;

/**
 * The FileErrorProvider class provides error information for classes that
 * operates on the file system, like {@link FileCopy} and {@link FileDelete}.
 * <p>
 * This class itself cannot be instantiated. Derived classes indicate errors
 * by calling the protected method {@link #setErrorInfo}.
 */
@SuppressWarnings("unused")
public abstract class FileErrorProvider {
    private FileError error = FileError.NONE;
    private String errorDescription = null;

    /**
     * Gets the code of the occurred FileError.
     *
     * @return The occurred error or {@link FileError#NONE} if no error occurred.
     */
    public FileError getError() {
        return error;
    }

    /**
     * Gets the string that describes the occurred FileError.
     *
     * @return The error description or {@code null} if no error occurred or
     * if no description is available.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Sets the {@link #getError error} to {@link FileError#NONE} and the
     * {@link #getErrorDescription description} to {@code null}.
     */
    protected void clearError() {
        error = FileError.NONE;
        errorDescription = null;
    }

    /**
     * Sets the specified error information.
     *
     * @param error            The error to set.
     * @param errorDescription The error description to set or {@code null} if
     *                         there is no description available.
     */
    protected void setErrorInfo(FileError error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
