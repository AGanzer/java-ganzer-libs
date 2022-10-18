package de.ganzer.core.files;

/**
 * Defines the possible behaviors after progress is reported by {@link FileDelete}.
 */
public enum DeleteProgressContinuation {
    /**
     * Deleting is continued. This should be the default result of the
     * function that receives progress information.
     */
    CONTINUE,

    /**
     * Deleting is completely canceled. The progress function is called  with the
     * status {@link DeleteProgressStatus#FINISHED}. {@link FileErrorProvider#getError()}
     * will return {@link FileError#CANCELED} after this.
     */
    CANCEL
}
