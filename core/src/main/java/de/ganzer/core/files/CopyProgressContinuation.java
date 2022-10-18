package de.ganzer.core.files;

/**
 * Defines the possible behaviors after progress is reported by {@link FileCopy}.
 */
public enum CopyProgressContinuation {
    /**
     * Copying is continued. This should be the default result of the
     * function that receives progress information.
     */
    CONTINUE,

    /**
     * If the status of the progress is {@link CopyProgressStatus#START_DIRECTORY}
     * or {@link CopyProgressStatus#START_FILE}, the file or directory is not copied
     * and ignored. If the status is {@link CopyProgressStatus#COPYING_FILE}, the
     * copied file is discarded and the original one (if present) is not
     * overwritten but restored.
     */
    SKIP,

    /**
     * Copying is completely canceled. The progress function is called with the
     * status {@link CopyProgressStatus#FINISHED}. {@link FileErrorProvider#getError()}
     * will return {@link FileError#CANCELED} after this. If the status of
     * the progress is {@link CopyProgressStatus#COPYING_FILE}, the copied file
     * is discarded  and the original one (if present) is not overwritten but restored.
     */
    CANCEL
}
