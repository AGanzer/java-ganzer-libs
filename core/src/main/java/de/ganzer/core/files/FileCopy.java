package de.ganzer.core.files;

import de.ganzer.core.CoreMessages;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FileCopy extends FileErrorProvider {
    /**
     * Defines the possible behaviors on existing files and directories.
     */
    public enum OverwriteAction {
        /**
         * The currently queried file or directory is not overwritten. The next
         * one is queried again.
         */
        OVERWRITE_NOT,

        /**
         * No file or directory is overwritten. No further query will occur.
         */
        OVERWRITE_NONE,

        /**
         * The currently queried file or directory is overwritten. The next one is
         * queried again.
         */
        OVERWRITE_ONE,

        /**
         * All files or directories are overwritten. No further query will occur.
         */
        OVERWRITE_ALL,

        /**
         * Cancels copying. {@link #getError()} will return {@link Error#CANCELED}
         * after finishing.
         */
        OVERWRITE_CANCEL
    }

    /**
     * The interface to a function that is called to query the user whether an
     * already existing file or directory shall be overwritten.
     */
    public interface QueryOverwriteAction {
        /**
         * Implementors should query the user what to do.
         *
         * @param source The file or directory to copy.
         * @param target The file or directory to overwrite.
         * @return One of the {@link OverwriteAction} values.
         */
        OverwriteAction query(File source, File target);
    }

    /**
     * Defines the possible reaction to perform at an occurred error.
     */
    public enum ErrorAction {
        /**
         * Copying is aborted completely.
         */
        ABORT,

        /**
         * Copying the file or directory that caused the error is retried.
         */
        RETRY,

        /**
         * The entry that caused the error is ignored and not copied.
         */
        IGNORE,

        /**
         * The entry that caused the error is ignored and not copied as well
         * as all further entries that will cause the same error.
         */
        IGNORE_ALL_THIS,

        /**
         * The entry that caused the error is ignored and not copied as well
         * as all further entries that will cause any error.
         */
        IGNORE_ALL
    }

    /**
     * The interface to a function that is called to query the user what to do
     * on the specified error.
     */
    public interface QueryErrorAction {
        /**
         * Implementors should query the user what to do.
         *
         * @param error            The code of the error that occurred.
         * @param errorDescription The description of the occurred error or
         *                         {@code null} if no description is available.
         * @param source           The file or directory to copy.
         * @param target           The file or directory to overwrite.
         * @return One of the {@link ErrorAction} values.
         */
        ErrorAction query(Error error, String errorDescription, File source, File target);
    }

    /**
     * Defines the status of the progress.
     */
    public enum ProgressStatus {
        /**
         * The copying operation is initialized and the total bytes to copy are
         * counted. The progress function is called once at startup with 0 counted
         * bytes and later for each directory and file that is worked.
         * <p>
         * This status is never reported if the initialization is suppressed via
         * {@link #start}.
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

    /**
     * The ProgressInfo class encapsulates progress information.
     */
    public class ProgressInfo {
        private final FileCopy machine;
        private final ProgressStatus status;
        private final String sourcePath;
        private final String targetPath;
        private final String rootSourcePath;
        private final String rootTargetPath;
        private final long fileBytesAvail;
        private final long fileBytesCopied;
        private final long totalBytesAvail;
        private final long totalBytesCopied;

        /**
         * Creates a new instance.
         */
        public ProgressInfo(FileCopy machine, ProgressStatus status, String sourcePath, String targetPath, String rootSourcePath, String rootTargetPath, long fileBytesAvail, long fileBytesCopied, long totalBytesAvail, long totalBytesCopied) {
            this.machine = machine;
            this.status = status;
            this.sourcePath = sourcePath;
            this.targetPath = targetPath;
            this.rootSourcePath = rootSourcePath;
            this.rootTargetPath = rootTargetPath;
            this.fileBytesAvail = fileBytesAvail;
            this.fileBytesCopied = fileBytesCopied;
            this.totalBytesAvail = totalBytesAvail;
            this.totalBytesCopied = totalBytesCopied;
        }

        /**
         * Gets the copy machine that has generated the progress report.
         *
         * @return The machine that has generated the report.
         */
        public FileCopy getMachine() {
            return machine;
        }

        /**
         * Gets the status of the progress.
         *
         * @return The current status.
         */
        public ProgressStatus getStatus() {
            return status;
        }

        /**
         * Gets the path to the file or directory that is currently copied.
         * <p>
         * If {@link #getStatus()} is {@link ProgressStatus#INITIALIZING}, this
         * is the path to the directory or file that's bytes are going to be
         * counted. This is an empty string if the progress function is called
         * first and no directory or file is worked yet.
         * <p>
         * If {@link #getStatus()} is {@link ProgressStatus#START_DIRECTORY},
         * this is the path to the directory that is going to be copied.
         * <p>
         * If {@link #getStatus()} is {@link ProgressStatus#START_FILE},
         * {@link ProgressStatus#COPYING_FILE} or {@link ProgressStatus#FINISHED_FILE},
         * this is the path to the file that is copied.
         *
         * @return The path to the source that is currently copied or an empty
         * string if {@link #getStatus()} is {@link ProgressStatus#INITIALIZING}
         * and the progress is reported first or if {@link #getStatus()} is
         * {@link ProgressStatus#FINISHED}.
         */
        public String getSourcePath() {
            return sourcePath;
        }

        /**
         * Gets the path to the destination file or directory that is currently
         * copied.
         * <p>
         * If {@link #getStatus()} is {@link ProgressStatus#START_DIRECTORY}, this is
         * the path to the directory where the directory to copy shall be written into.
         * <p>
         * If {@link #getStatus()} is {@link ProgressStatus#START_FILE},
         * {@link ProgressStatus#COPYING_FILE} or {@link ProgressStatus#FINISHED_FILE},
         * this is the path to the file where the source file is copied into.
         *
         * @return The path to the destination where data is currently copied
         * into or an empty string if {@link #getStatus()} is
         * {@link ProgressStatus#INITIALIZING} and the progress is reported first
         * or if {@link #getStatus()} is {@link ProgressStatus#FINISHED}.
         */
        public String getTargetPath() {
            return targetPath;
        }

        /**
         * Gets the path to the file or directory that was specified as source
         * to start copying operation.
         *
         * @return The path to the original source that is currently worked or an
         * empty string if {@link #getStatus()} is {@link ProgressStatus#INITIALIZING}.
         */
        public String getRootSourcePath() {
            return rootSourcePath;
        }

        /**
         * Gets the path to the directory that was specified as target to start
         * copying operation.
         *
         * @return The path to the original target.
         */
        public String getRootTargetPath() {
            return rootTargetPath;
        }

        /**
         * The size of the file that is currently copied.
         * <p>
         * This value is valid only if {@link #getStatus()} is either
         * {@link ProgressStatus#START_FILE}, {@link ProgressStatus#COPYING_FILE} or
         * {@link ProgressStatus#FINISHED_FILE}.
         *
         * @return The size of the file in bytes.
         */
        public long getFileBytesAvail() {
            return fileBytesAvail;
        }

        /**
         * The number of bytes that are already copied from the current file.
         * <p>
         * This value is valid only if {@link #getStatus()} is either
         * {@link ProgressStatus#START_FILE}, {@link ProgressStatus#COPYING_FILE} or
         * {@link ProgressStatus#FINISHED_FILE}.
         *
         * @return The number of already copied bytes.
         */
        public long getFileBytesCopied() {
            return fileBytesCopied;
        }

        /**
         * The number of all bytes that have to be copied.
         * <p>
         * This is the number of currently counted bytes that have to be copied
         * as long as {@link #getStatus()} is {@link ProgressStatus#INITIALIZING}.
         * <p>
         * This value is valid only if the initialization is not suppressed via
         * {@link #start()}.
         *
         * @return The number of all available bytes.
         */
        public long getTotalBytesAvail() {
            return totalBytesAvail;
        }

        /**
         * The total number of already copied bytes.
         * <p>
         * This value is valid only if {@link #getStatus()} is other than
         * {@link ProgressStatus#INITIALIZING}.
         *
         * @return The number of already copied bytes.
         */
        public long getTotalBytesCopied() {
            return totalBytesCopied;
        }

        /**
         * Gets the percentage of already copied bytes from the current file.
         * <p>
         * This value is valid only if {@link #getStatus()} is either
         * {@link ProgressStatus#START_FILE}, {@link ProgressStatus#COPYING_FILE} or
         * {@link ProgressStatus#FINISHED_FILE}.
         *
         * @return The percentage [0-100].
         */
        public double getFilePercentage() {
            return fileBytesCopied * 100.0 / fileBytesAvail;
        }

        /**
         * Gets the percentage of all already copied bytes.
         * <p>
         * This value is valid only if {@link #getStatus()} is other than
         * {@link ProgressStatus#INITIALIZING} and if the initialization is not
         * suppressed via {@link #start()}.
         *
         * @return The percentage [0-100].
         */
        public double getTotalPercentage() {
            return totalBytesCopied * 100.0 / totalBytesAvail;
        }
    }

    /**
     * Defines the possible behaviors after progress is reported.
     */
    public enum ProgressContinuation {
        /**
         * Copying is continued. This should be the default result of the
         * function that receives progress information.
         */
        CONTINUE_COPY,

        /**
         * If the status of the progress is {@link ProgressStatus#START_DIRECTORY}
         * or {@link ProgressStatus#START_FILE}, the file or directory is not copied
         * and ignored. If the status is {@link ProgressStatus#COPYING_FILE}, the
         * copied file is discarded and the original one (if present) is not
         * overwritten but restored.
         */
        SKIP_ENTRY,

        /**
         * Copying is completely canceled. The progress function is called with the
         * status {@link ProgressStatus#FINISHED}. {@link #getError()} will return
         * {@link Error#CANCELED} after this. If the status of the progress is
         * {@link ProgressStatus#COPYING_FILE}, the copied file is discarded and
         * the original one (if present) is not overwritten but restored.
         */
        CANCEL_COPY
    }

    /**
     * The interface to a fanction that is called to report the progress.
     */
    public interface ProgressFunction {
        /**
         * The called function.
         *
         * @param info The information about the progress.
         * @return One of the {@link ProgressContinuation} values.
         */
        ProgressContinuation report(ProgressInfo info);
    }

    /**
     * An interface to a function that is called to get an alternative path to
     * an already existing file or directory where to copy data into.
     * <p>
     * This function is called only for a source file or directory that is
     * located directly in the specified target directory. The path returned
     * by this function may contain a hint in its name like "Copy" or "Copy 2"
     * or may even point to another directory.
     */
    public interface AlternativeTargetPathFunction {
        /**
         * The called function.
         *
         * @param originalPath The path to the already existing file or directory.
         * @return The path to the target where to copy the data into. This may be
         * the same as originalPath.
         */
        String alternativePath(String originalPath);
    }

    private final ProgressFunction progressFunction;
    private final QueryErrorAction queryErrorAction;
    private final QueryOverwriteAction queryOverwriteAction;
    private final AlternativeTargetPathFunction alternativeTargetPathFunction;
    private OverwriteAction defaultFileOverwriteAction = OverwriteAction.OVERWRITE_NONE;
    private OverwriteAction defaultDirOverwriteAction = OverwriteAction.OVERWRITE_NONE;
    private int copyBufferSize = 8 * 1024;
    private FileFilter fileFilter;
    private FilenameFilter filenameFilter;

    /**
     * Creates a new instance.
     *
     * @param progressFunction The function to call for each file that is
     *                         going to be copied. If this is {@code null},
     *                         the progress is not reported.
     */
    public FileCopy(ProgressFunction progressFunction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = null;
        this.queryOverwriteAction = null;
        this.alternativeTargetPathFunction = null;
    }

    /**
     * Creates a new instance.
     *
     * @param progressFunction     The function to call for each file that is
     *                             going to be copied. If this is {@code null},
     *                             the progress is not reported.
     * @param queryOverwriteAction The function to call if a target does
     *                             already exist. If this is {@code null},
     *                             the target is overwritten only if
     *                             {@link #getDefaultFileOverwriteAction}
     *                             respective {@link #getDefaultDirOverwriteAction}
     *                             returns {@link OverwriteAction#OVERWRITE_ALL}.
     */
    public FileCopy(ProgressFunction progressFunction, QueryOverwriteAction queryOverwriteAction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = null;
        this.queryOverwriteAction = queryOverwriteAction;
        this.alternativeTargetPathFunction = null;
    }

    /**
     * Creates a new instance.
     *
     * @param progressFunction The function to call for each file that is
     *                         going to be copied. If this is {@code null},
     *                         the progress is not reported.
     * @param queryErrorAction The function to call when an error occurred.
     *                         If this is {@code null}, each error aborts
     *                         copying completely.
     */
    public FileCopy(ProgressFunction progressFunction, QueryErrorAction queryErrorAction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = queryErrorAction;
        this.queryOverwriteAction = null;
        this.alternativeTargetPathFunction = null;
    }

    /**
     * Creates a new instance.
     *
     * @param progressFunction     The function to call for each file that is
     *                             going to be copied. If this is {@code null},
     *                             the progress is not reported.
     * @param queryErrorAction     The function to call when an error occurred.
     *                             If this is {@code null}, each error aborts
     *                             copying completely.
     * @param queryOverwriteAction The function to call if a target does
     *                             already exist. If this is {@code null},
     *                             the target is overwritten only if
     *                             {@link #getDefaultFileOverwriteAction}
     *                             respective {@link #getDefaultDirOverwriteAction}
     *                             returns {@link OverwriteAction#OVERWRITE_ALL}.
     */
    public FileCopy(ProgressFunction progressFunction, QueryErrorAction queryErrorAction, QueryOverwriteAction queryOverwriteAction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = queryErrorAction;
        this.queryOverwriteAction = queryOverwriteAction;
        this.alternativeTargetPathFunction = null;
    }

    /**
     * Creates a new instance.
     *
     * @param progressFunction              The function to call for each file that is
     *                                      going to be copied. If this is {@code null},
     *                                      the progress is not reported.
     * @param queryErrorAction              The function to call when an error occurred.
     *                                      If this is {@code null}, each error aborts
     *                                      copying completely.
     * @param queryOverwriteAction          The function to call if a target does
     *                                      already exist. If this is {@code null},
     *                                      the target is overwritten only if
     *                                      {@link #getDefaultFileOverwriteAction}
     *                                      respective {@link #getDefaultDirOverwriteAction}
     *                                      returns {@link OverwriteAction#OVERWRITE_ALL}.
     * @param alternativeTargetPathFunction The function to call when an alternative
     *                                      path is requested. If this is {@code null},
     *                                      the original path is used.
     */
    public FileCopy(ProgressFunction progressFunction, QueryErrorAction queryErrorAction, QueryOverwriteAction queryOverwriteAction, AlternativeTargetPathFunction alternativeTargetPathFunction) {
        this.progressFunction = progressFunction;
        this.queryErrorAction = queryErrorAction;
        this.queryOverwriteAction = queryOverwriteAction;
        this.alternativeTargetPathFunction = alternativeTargetPathFunction;
    }

    /**
     * Gets the default overwrite action for files.
     *
     * @return The default overwrite action for files. The constructor
     * sets this to {@link OverwriteAction#OVERWRITE_NONE}.
     */
    public OverwriteAction getDefaultFileOverwriteAction() {
        return defaultFileOverwriteAction;
    }

    /**
     * Sets the default overwrite action for file.
     *
     * @param defaultFileOverwriteAction The default action to set.
     */
    public void setDefaultFileOverwriteAction(OverwriteAction defaultFileOverwriteAction) {
        this.defaultFileOverwriteAction = defaultFileOverwriteAction;
    }

    /**
     * Gets the default overwrite action for directories.
     *
     * @return The default overwrite action for directories. The constructor
     * sets this to {@link OverwriteAction#OVERWRITE_NONE}.
     */
    public OverwriteAction getDefaultDirOverwriteAction() {
        return defaultDirOverwriteAction;
    }

    /**
     * Sets the default overwrite action for directories.
     *
     * @param defaultDirOverwriteAction The default action to set.
     */
    public void setDefaultDirOverwriteAction(OverwriteAction defaultDirOverwriteAction) {
        this.defaultDirOverwriteAction = defaultDirOverwriteAction;
    }

    /**
     * Gets the size of the buffer that is used to copy a file.
     * <p>
     * The data that fits into the buffer is read and written as a block. The
     * user cannot cancel the operation while a buffer is moved from one file
     * to another. Progress information is reported only after a buffer is
     * completely worked and before (if there is more data avail) the next
     * block of data is copied.
     *
     * @return The size of the buffer in bytes. The default value is 8 KB.
     */
    public int getCopyBufferSize() {
        return copyBufferSize;
    }

    /**
     * Sets the size of the buffer that is used to copy a file.
     * <p>
     * The data that fits into the buffer is read and written as a block. The
     * user cannot cancel the operation while a buffer is moved from one file
     * to another. Progress information is reported only after a buffer is
     * completely worked and before (if there is more data avail) the next
     * block of data is copied.
     *
     * @param copyBufferSize The new size to set.
     */
    public void setCopyBufferSize(int copyBufferSize) {
        this.copyBufferSize = copyBufferSize;
    }

    /**
     * Gets the file filter used for iterating through directories.
     * <p>
     * The filter is not applied to the entries that are given to
     * {@link #start} but only for entries in subdirectories.
     *
     * @return The used filter or {@code null} if not filter is used.
     */
    public FileFilter getFileFilter() {
        return fileFilter;
    }

    /**
     * Sets the file filter used for iterating through directories.
     * <p>
     * The filter is not applied to the entries that are given to
     * {@link #start} but only for entries in subdirectories.
     *
     * @param fileFilter The filter to use or {@code null} to use no filter.
     */
    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
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

    private class ErrorInfo extends RuntimeException {
        private final boolean queryHandling;
        private final Error error;

        public ErrorInfo(Error error, String errorDescription, boolean queryHandling) {
            super(errorDescription);

            this.queryHandling = queryHandling;
            this.error = error;
        }

        public boolean isQueryHandling() {
            return queryHandling;
        }

        public Error getError() {
            return error;
        }

        public String getErrorDescription() {
            return getMessage();
        }
    }

    /**
     * Copies the specified file or directory.
     *
     * @param source       The absolute or relative path to the file or directory to
     *                     copy.
     * @param target       The absolute or relative path to the directory where to
     *                     copy into.
     * @param suppressInit The counting of all bytes to copy is suppressed if
     *                     this is {@code true}. Neither {@link ProgressInfo#totalBytesAvail}
     *                     nor {@link ProgressInfo#getTotalPercentage} can be used
     *                     when progress is reported.
     * @return {@code true} on success; otherwise, {@code false} is returned.
     */
    public boolean start(String source, String target, boolean suppressInit) {
        return start(Collections.singletonList(source), target, suppressInit);
    }

    /**
     * Copies the specified files or directories.
     *
     * @param sources      The absolute or relative paths to the files or directories
     *                     to copy.
     * @param target       The absolute or relative path to the directory where to
     *                     copy into.
     * @param suppressInit The counting of all bytes to copy is suppressed if
     *                     this is {@code true}. Neither {@link ProgressInfo#totalBytesAvail}
     *                     nor {@link ProgressInfo#getTotalPercentage} can be used
     *                     when progress is reported.
     * @return {@code true} on success; otherwise, {@code false} is returned.
     */
    public boolean start(List<String> sources, String target, boolean suppressInit) {
        var sourceFiles = sources.stream().map(File::new);
        var targetFile = new File(target);

        clearError();

        try {
            verifyTargetType(targetFile);
            verifyExistence(sourceFiles, targetFile);
            verifyNonRecursive(sourceFiles, targetFile);

            initializeCopy(sourceFiles, targetFile, suppressInit);
            copyEntries(sourceFiles, targetFile);
        } catch (ErrorInfo info) {
            setErrorInfo(info.getError(), info.getErrorDescription());
        }
        return false;
    }

    private void verifyTargetType(File targetFile) throws ErrorInfo {
        if (targetFile.isFile())
            throw new ErrorInfo(Error.TARGET_TYPE, String.format(CoreMessages.get("invalidCopyTarget"), targetFile.getAbsolutePath()), true);
    }

    private void verifyExistence(Stream<File> sourceFiles, File targetFile) throws ErrorInfo {
        sourceFiles.forEach(this::verifySourceExistence);
        verifyDestExistence(targetFile);
    }

    private void verifySourceExistence(File sourceFile) throws ErrorInfo {
        if( !sourceFile.exists() )
            throw new ErrorInfo(Error.SOURCE_NOT_EXIST, String.format(CoreMessages.get("sourceFileDoesNotExist"), sourceFile.getAbsolutePath()), true);
    }

    private void verifyDestExistence(File targetFile) throws ErrorInfo {
        if (!targetFile.exists() && !targetFile.mkdirs())
            throw new ErrorInfo(Error.CREATE_DIR, String.format(CoreMessages.get("cannotCreateDir"), targetFile.getAbsolutePath()), true);
    }

    private void verifyNonRecursive(Stream<File> sourceFiles, File targetFile) throws ErrorInfo {
        sourceFiles.forEach(file -> verifyNonRecursive(file, targetFile));
    }

    private void verifyNonRecursive(File sourceFile, File targetFile) throws ErrorInfo {
        var sourcePath = Path.of(sourceFile.getAbsolutePath());
        var targetPath = Path.of(sourceFile.getAbsolutePath());

        if (targetPath.equals(sourcePath) || targetPath.startsWith(sourcePath))
            throw new ErrorInfo(Error.CREATE_DIR, String.format(CoreMessages.get("cannotCopyIntoItself"), sourceFile.getAbsolutePath()), true);
    }

    private void initializeCopy(Stream<File> sourceFiles, File targetFile, boolean suppressInit) throws ErrorInfo {

    }

    private void copyEntries(Stream<File> sourceFiles, File targetFile) throws ErrorInfo {

    }

    private void cancel() throws ErrorInfo {
        throw new ErrorInfo(Error.CANCELED, CoreMessages.get("operationCanceld"), true);
    }
}
