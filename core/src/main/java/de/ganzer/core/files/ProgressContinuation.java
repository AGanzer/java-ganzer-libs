package de.ganzer.core.files;

/**
 * Defines the possible behaviors after progress is reported by
 * {@link FileCopy} or {@link FileDelete}.
 */
public enum ProgressContinuation {
    /**
     * Copying is continued. This should be the default result of the
     * function that receives progress information.
     */
    CONTINUE,

    /**
     * If the status of the progress is {@link ProgressStatus#START_DIRECTORY}
     * or {@link ProgressStatus#START_FILE}, the file or directory is not copied
     * and ignored. If the status is {@link ProgressStatus#COPYING_FILE}, the
     * copied file is discarded and the original one (if present) is not
     * overwritten but restored.
     */
    SKIP,

    /**
     * Copying or deleting is completely canceled. The progress function is called
     * with the status {@link ProgressStatus#FINISHED}. {@link FileErrorProvider#getError()}
     * will return {@link FileErrorProvider.Error#CANCELED} after this. If the status of
     * the progress is {@link ProgressStatus#COPYING_FILE}, the copied file is discarded
     * and the original one (if present) is not overwritten but restored.
     */
    CANCEL
}
