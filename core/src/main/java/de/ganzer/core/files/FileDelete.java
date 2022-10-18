package de.ganzer.core.files;

import de.ganzer.core.CoreMessages;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class FileDelete extends FileErrorProvider {
    /**
     * The interface to a function that is called to query the user what to do
     * on the specified FileError.
     */
    public interface QueryErrorAction {
        /**
         * Implementors should query the user what to do.
         *
         * @param error            The code of the error that occurred.
         * @param errorDescription The description of the occurred error or
         *                         {@code null} if no description is available.
         * @param file             The file or directory to delete.
         * @return One of the {@link DeleteErrorAction} values.
         */
        DeleteErrorAction query(FileError error, String errorDescription, File file);
    }

    /**
     * The ProgressInfo class encapsulates progress information.
     */
    @SuppressWarnings("unused")
    public static class ProgressInfo {
        private final FileDelete machine;
        private DeleteProgressStatus status;
        private String path;
        private String rootPath;
        private long entriesAvail;
        private long entriesDeleted;

        /**
         * Creates a new instance.
         */
        public ProgressInfo(FileDelete machine) {
            this.machine = machine;
        }

        /**
         * Gets the machine that has generated the progress report.
         *
         * @return The machine that has generated the report.
         */
        public FileDelete getMachine() {
            return machine;
        }

        /**
         * Gets the status of the progress.
         *
         * @return The current status.
         */
        public DeleteProgressStatus getStatus() {
            return status;
        }

        /**
         * Gets the path to the file or directory that is currently deleted.
         * <p>
         * If {@link #getStatus()} is {@link DeleteProgressStatus#INITIALIZING}, this
         * is the path to the directory or file that is going to be counted. This
         * is an empty string if the progress function is called first and no
         * directory or file is counted yet.
         * <p>
         * If {@link #getStatus()} is {@link DeleteProgressStatus#DELETE_DIRECTORY},
         * this is the path to the directory that is going to be deleted.
         * <p>
         * If {@link #getStatus()} is {@link DeleteProgressStatus#DELETE_FILE},
         * this is the path to the file that is copied.
         *
         * @return The path to the entry that is currently deleted or an empty
         * string if {@link #getStatus()} is {@link DeleteProgressStatus#INITIALIZING}
         * and the progress is reported first or if {@link #getStatus()} is
         * {@link DeleteProgressStatus#FINISHED}.
         */
        public String getPath() {
            return path;
        }

        /**
         * Gets the path to the file or directory that was specified as source
         * to start deleting operation.
         *
         * @return The path to the original source that is currently worked or an
         * empty string if {@link #getStatus()} is {@link DeleteProgressStatus#INITIALIZING}.
         */
        public String getRootPath() {
            return rootPath;
        }

        /**
         * The number of all entries that have to be deleted.
         * <p>
         * This is the number of currently counted entries that have to be deleted
         * as long as {@link #getStatus()} is {@link DeleteProgressStatus#INITIALIZING}.
         * <p>
         * This value is valid only if the initialization is not suppressed via
         * {@link #start}.
         *
         * @return The number of all available bytes.
         */
        public long getEntriesAvail() {
            return entriesAvail;
        }

        /**
         * The total number of already deleted entries.
         * <p>
         * This value is valid only if {@link #getStatus()} is other than
         * {@link DeleteProgressStatus#INITIALIZING}.
         *
         * @return The number of already copied bytes.
         */
        public long getEntriesDeleted() {
            return entriesDeleted;
        }

        /**
         * Gets the percentage of all already deleted entries.
         * <p>
         * This value is valid only if {@link #getStatus()} is other than
         * {@link DeleteProgressStatus#INITIALIZING} and if the initialization
         * is not suppressed via {@link #start}.
         *
         * @return The percentage [0-100].
         */
        public double getPercentage() {
            return entriesDeleted * 100.0 / entriesAvail;
        }
    }

    /**
     * The interface to a function that is called to report the progress.
     */
    public interface ProgressFunction {
        /**
         * The called function.
         *
         * @param info The information about the progress.
         * @return One of the {@link DeleteProgressContinuation} values.
         */
        DeleteProgressContinuation report(ProgressInfo info);
    }

    private final ProgressFunction progressFunction;
    private final QueryErrorAction queryErrorAction;
    private final ProgressInfo progress = new ProgressInfo(this);
    private FilenameFilter filenameFilter;

    /**
     * Creates a new instance.
     *
     * @param progressFunction The function to call for each file that is
     *                         going to be copied. If this is {@code null},
     *                         the progress is not reported.
     */
    public FileDelete(ProgressFunction progressFunction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = null;
    }

    /**
     * Creates a new instance.
     *
     * @param progressFunction The function to call for each file that is
     *                         going to be copied. If this is {@code null},
     *                         the progress is not reported.
     * @param queryErrorAction The function to call when an error occurred.
     *                         If this is {@code null}, each error aborts
     *                         deleting.
     */
    public FileDelete(ProgressFunction progressFunction, QueryErrorAction queryErrorAction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = queryErrorAction;
    }

    /**
     * Gets the filename filter used for iterating through directories.
     * <p>
     * The filter is not applied to the entries that are given to
     * {@link #start} but only for entries in subdirectories.
     *
     * @return The used filter or {@code null} if not filter is used.
     */
    public FilenameFilter getFilenameFilter() {
        return filenameFilter;
    }

    /**
     * Sets the filename filter used for iterating through directories.
     * <p>
     * The filter is not applied to the entries that are given to
     * {@link #start} but only for entries in subdirectories.
     *
     * @param filenameFilter The filter to use or {@code null} to use no filter.
     */
    public void setFilenameFilter(FilenameFilter filenameFilter) {
        this.filenameFilter = filenameFilter;
    }

    /**
     * Copies the specified file or directory.
     *
     * @param source       The absolute or relative path to the file or directory to
     *                     delete.
     * @param suppressInit The counting of all entries to delete is suppressed if
     *                     this is {@code true}. Neither {@link FileDelete.ProgressInfo#getEntriesAvail}
     *                     nor {@link FileDelete.ProgressInfo#getPercentage} can be used
     *                     when progress is reported.
     * @return {@code true} on success; otherwise, {@code false} is returned.
     */
    public boolean start(String source, boolean suppressInit) {
        return start(Collections.singletonList(source), suppressInit);
    }

    /**
     * Copies the specified files or directories.
     *
     * @param sources      The absolute or relative paths to the files or directories
     *                     to delete.
     * @param suppressInit The counting of all entries to delete is suppressed if
     *                     this is {@code true}. Neither {@link FileDelete.ProgressInfo#getEntriesAvail}
     *                     nor {@link FileDelete.ProgressInfo#getPercentage} can be used
     *                     when progress is reported.
     * @return {@code true} on success; otherwise, {@code false} is returned.
     */
    public boolean start(List<String> sources, boolean suppressInit) {
        var sourceFiles = sources.stream().map(File::new).collect(Collectors.toList());

        clearError();

        try {
            verifyExistence(sourceFiles);
            initializeDelete(sourceFiles, suppressInit);
            deleteEntries(sourceFiles);
        } catch (ErrorInfo info) {
            setErrorInfo(info.getError(), info.getErrorDescription());
        }

        reportFinished();

        return

                getError() != FileError.NONE;
    }

    private static class ErrorInfo extends RuntimeException {
        private final boolean queryHandling;
        private final FileError error;

        public ErrorInfo(FileError error, String errorDescription, boolean queryHandling) {
            super(errorDescription);

            this.queryHandling = queryHandling;
            this.error = error;
        }

        public boolean doNotQuery() {
            return !queryHandling;
        }

        public FileError getError() {
            return error;
        }

        public String getErrorDescription() {
            return getMessage();
        }
    }

    private void verifyExistence(List<File> sourceFiles) throws ErrorInfo {
        sourceFiles.forEach(this::verifySourceExistence);
    }

    private void verifySourceExistence(File sourceFile) throws ErrorInfo {
        if (!sourceFile.exists())
            throw new ErrorInfo(FileError.FILE_NOT_EXIST, String.format(CoreMessages.get("fileDoesNotExist"), sourceFile.getAbsolutePath()), true);
    }

    private void initializeDelete(List<File> sourceFiles, boolean suppressInit) throws ErrorInfo {
        progress.status = DeleteProgressStatus.INITIALIZING;
        progress.entriesAvail = 0;
        progress.entriesDeleted = 0;
        progress.path = "";

        if (suppressInit)
            return;

        reportProgress();

        sourceFiles.forEach(source -> {
            progress.rootPath = source.getAbsolutePath();

            if (source.isDirectory())
                initializeDelete(source);
            else
                reportInitializeProgress(source.getAbsolutePath(), 1);
        });
    }

    private void initializeDelete(File source) {
        reportInitializeProgress(source.getAbsolutePath(), 1);

        for (var file : listFiles(source)) {
            if (isDirectory(file))
                initializeDelete(file);
            else
                reportInitializeProgress(file.getAbsolutePath(), file.length());
        }
    }

    private void deleteEntries(List<File> sourceFiles) throws ErrorInfo {
        sourceFiles.forEach(source -> {
            progress.rootPath = source.getAbsolutePath();

            if (isDirectory(source)) {
                deleteDir(source);
                reportDelete(source, DeleteProgressStatus.DELETE_DIRECTORY);
            } else {
                deleteFile(source);
            }
        });
    }

    @SuppressWarnings("DuplicatedCode")
    private void deleteFile(File source) {
        reportDelete(source, DeleteProgressStatus.DELETE_FILE);

        while (true) {
            try {
                deleteFileNoQuery(source);
                break;
            } catch (ErrorInfo info) {
                if (queryErrorAction == null || info.doNotQuery())
                    throw info;

                switch (queryErrorAction.query(info.getError(), info.getErrorDescription(), source)) {
                    case RETRY:
                        continue;

                    case IGNORE:
                        return;

                    default:
                        throw new ErrorInfo(info.getError(), info.getErrorDescription(), false);
                }
            }
        }
    }

    private void deleteFileNoQuery(File source) {
        try {
            if (!source.delete())
                throw new ErrorInfo(FileError.DELETE_DIR, String.format(CoreMessages.get("cannotDeleteFile"), source.getAbsolutePath()), true);
        } catch (SecurityException e) {
            throw new ErrorInfo(FileError.ACCESS, String.format(CoreMessages.get("accessDenied"), source.getAbsolutePath()), true);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void deleteDir(File source) {
        while (true) {
            try {
                deleteDirNoQuery(source);
                break;
            } catch (ErrorInfo info) {
                if (queryErrorAction == null || info.doNotQuery())
                    throw info;

                switch (queryErrorAction.query(info.getError(), info.getErrorDescription(), source)) {
                    case RETRY:
                        continue;

                    case IGNORE:
                        return;

                    default:
                        throw new ErrorInfo(info.getError(), info.getErrorDescription(), false);
                }
            }
        }
    }

    private void deleteDirNoQuery(File source) {
        for (var file : listFiles(source)) {
            if (isDirectory(file)) {
                deleteDir(file);
                reportDelete(file, DeleteProgressStatus.DELETE_DIRECTORY);

                try {
                    if (!file.delete())
                        throw new ErrorInfo(FileError.DELETE_DIR, String.format(CoreMessages.get("cannotDeleteDir"), file.getAbsolutePath()), true);
                } catch (SecurityException e) {
                    throw new ErrorInfo(FileError.ACCESS, String.format(CoreMessages.get("accessDenied"), file.getAbsolutePath()), true);
                }
            } else {
                deleteFile(file);
            }
        }
    }

    private boolean isDirectory(File file) throws ErrorInfo {
        try {
            return file.isDirectory();
        } catch (SecurityException e) {
            throw new ErrorInfo(FileError.ACCESS, String.format(CoreMessages.get("accessDenied"), file.getAbsolutePath()), true);
        }
    }

    private File[] listFiles(File source) {
        try {
            return filenameFilter == null
                    ? source.listFiles()
                    : source.listFiles(filenameFilter);
        } catch (SecurityException e) {
            throw new ErrorInfo(FileError.ACCESS, String.format(CoreMessages.get("accessDenied"), source.getAbsolutePath()), true);
        }
    }

    private void reportProgress() {
        if (progressFunction == null)
            return;

        DeleteProgressContinuation result = progressFunction.report(progress);

        if (result == DeleteProgressContinuation.CANCEL)
            cancel();
    }

    private void reportInitializeProgress(String sourcePath, long addEntries) {
        progress.path = sourcePath;
        progress.entriesAvail += addEntries;

        reportProgress();
    }

    private void reportDelete(File source, DeleteProgressStatus status) {
        progress.status = status;
        progress.path = source.getAbsolutePath();
        ++progress.entriesDeleted;

        reportProgress();
    }

    private void reportFinished() {
        progress.status = DeleteProgressStatus.FINISHED;
        progress.path = "";
        progress.rootPath = "";

        reportProgress();
    }

    private void cancel() throws ErrorInfo {
        throw new ErrorInfo(FileError.CANCELED, CoreMessages.get("operationCanceled"), true);
    }
}
