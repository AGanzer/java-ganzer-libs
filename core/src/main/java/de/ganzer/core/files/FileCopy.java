package de.ganzer.core.files;

import de.ganzer.core.CoreMessages;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class FileCopy extends FileErrorProvider {
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
     * The ProgressInfo class encapsulates progress information.
     */
    public static class ProgressInfo {
        private final FileCopy machine;
        private ProgressStatus status;
        private String sourcePath;
        private String targetPath;
        private String rootSourcePath;
        private String rootTargetPath;
        private long fileBytesAvail;
        private long fileBytesCopied;
        private long totalBytesAvail;
        private long totalBytesCopied;

        private OverwriteAction fileOverwriteAction;
        private OverwriteAction dirOverwriteAction;
        private boolean ignoreAllErrors;
        private final Set<Error> ignoredErrors = new HashSet<>();

        /**
         * Creates a new instance.
         */
        public ProgressInfo(FileCopy machine) {
            this.machine = machine;
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
         * {@link #start}.
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
         * suppressed via {@link #start}.
         *
         * @return The percentage [0-100].
         */
        public double getTotalPercentage() {
            return totalBytesCopied * 100.0 / totalBytesAvail;
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
    private final ProgressInfo progress = new ProgressInfo(this);
    private OverwriteAction defaultFileOverwriteAction = OverwriteAction.NOT;
    private OverwriteAction defaultDirOverwriteAction = OverwriteAction.NOT;
    private byte[] copyBuffer = new byte[8 * 1024];
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
     *                             returns {@link OverwriteAction#NOT}.
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
     *                             returns {@link OverwriteAction#NOT}.
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
     *                                      returns {@link OverwriteAction#NOT}.
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
     * sets this to {@link OverwriteAction#NOT}.
     */
    public OverwriteAction getDefaultFileOverwriteAction() {
        return defaultFileOverwriteAction;
    }

    /**
     * Sets the default overwrite action for file.
     *
     * @param defaultFileOverwriteAction The default action to set.
     * @throws NullPointerException defaultFileOverwriteAction is {@code null}.
     */
    public void setDefaultFileOverwriteAction(OverwriteAction defaultFileOverwriteAction) {
        if (defaultDirOverwriteAction == null)
            throw new NullPointerException("defaultDirOverwriteAction");

        this.defaultFileOverwriteAction = defaultFileOverwriteAction;
    }

    /**
     * Gets the default overwrite action for directories.
     *
     * @return The default overwrite action for directories. The constructor
     * sets this to {@link OverwriteAction#NOT}.
     */
    public OverwriteAction getDefaultDirOverwriteAction() {
        return defaultDirOverwriteAction;
    }

    /**
     * Sets the default overwrite action for directories.
     *
     * @param defaultDirOverwriteAction The default action to set.
     * @throws NullPointerException defaultFileOverwriteAction is {@code null}.
     */
    public void setDefaultDirOverwriteAction(OverwriteAction defaultDirOverwriteAction) {
        if (defaultDirOverwriteAction == null)
            throw new NullPointerException("defaultDirOverwriteAction");

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
        return copyBuffer.length;
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
        this.copyBuffer = new byte[copyBufferSize];
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
        var sourceFiles = sources.stream().map(File::new).collect(Collectors.toList());
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

        reportFinished();

        return getError() != Error.NONE;
    }

    private static class ErrorInfo extends RuntimeException {
        private final boolean queryHandling;
        private final Error error;

        public ErrorInfo(Error error, String errorDescription, boolean queryHandling) {
            super(errorDescription);

            this.queryHandling = queryHandling;
            this.error = error;
        }

        public boolean doNotQuery() {
            return !queryHandling;
        }

        public Error getError() {
            return error;
        }

        public String getErrorDescription() {
            return getMessage();
        }
    }

    private void verifyTargetType(File targetFile) throws ErrorInfo {
        if (targetFile.isFile())
            throw new ErrorInfo(Error.TARGET_TYPE, String.format(CoreMessages.get("invalidCopyTarget"), targetFile.getAbsolutePath()), true);
    }

    private void verifyExistence(List<File> sourceFiles, File targetFile) throws ErrorInfo {
        sourceFiles.forEach(this::verifySourceExistence);
        verifyDestExistence(targetFile);
    }

    private void verifySourceExistence(File sourceFile) throws ErrorInfo {
        if (!sourceFile.exists())
            throw new ErrorInfo(Error.SOURCE_NOT_EXIST, String.format(CoreMessages.get("sourceFileDoesNotExist"), sourceFile.getAbsolutePath()), true);
    }

    private void verifyDestExistence(File targetFile) throws ErrorInfo {
        if (!targetFile.exists() && !targetFile.mkdirs())
            throw new ErrorInfo(Error.CREATE_DIR, String.format(CoreMessages.get("cannotCreateDir"), targetFile.getAbsolutePath()), true);
    }

    private void verifyNonRecursive(List<File> sourceFiles, File targetFile) throws ErrorInfo {
        sourceFiles.forEach(file -> verifyNonRecursive(file, targetFile));
    }

    private void verifyNonRecursive(File sourceFile, File targetFile) throws ErrorInfo {
        var sourcePath = Path.of(sourceFile.getAbsolutePath());
        var targetPath = Path.of(targetFile.getAbsolutePath());

        if (targetPath.equals(sourcePath) || targetPath.startsWith(sourcePath))
            throw new ErrorInfo(Error.CREATE_DIR, String.format(CoreMessages.get("cannotCopyIntoItself"), sourceFile.getAbsolutePath()), true);
    }

    private void initializeCopy(List<File> sourceFiles, File targetFile, boolean suppressInit) throws ErrorInfo {
        progress.status = ProgressStatus.INITIALIZING;
        progress.rootTargetPath = targetFile.getAbsolutePath();
        progress.fileBytesAvail = 0;
        progress.fileBytesCopied = 0;
        progress.totalBytesAvail = 0;
        progress.totalBytesCopied = 0;
        progress.sourcePath = "";
        progress.targetPath = "";
        progress.dirOverwriteAction = defaultDirOverwriteAction;
        progress.fileOverwriteAction = defaultFileOverwriteAction;
        progress.ignoreAllErrors = false;
        progress.ignoredErrors.clear();

        if (suppressInit)
            return;

        reportProgress();

        sourceFiles.forEach(source -> {
            if (source.isDirectory())
                initializeCopy(source);
            else
                reportInitializeProgress(source.getAbsolutePath(), source.length());
        });
    }

    private boolean reportProgress() {
        if (progressFunction == null)
            return true;

        ProgressContinuation result = progressFunction.report(progress);

        if (result == ProgressContinuation.CANCEL)
            cancel();

        return result == ProgressContinuation.CONTINUE;
    }

    private boolean reportStartDir(String sourcePath, String targetPath) {
        progress.status = ProgressStatus.START_DIRECTORY;
        progress.sourcePath = sourcePath;
        progress.targetPath = targetPath;
        progress.fileBytesAvail = 0;
        progress.fileBytesCopied = 0;

        return reportProgress();
    }

    private void reportFinishedDir(String sourcePath, String targetPath) {
        progress.status = ProgressStatus.FINISHED_DIRECTORY;
        progress.sourcePath = sourcePath;
        progress.targetPath = targetPath;
        progress.fileBytesAvail = 0;
        progress.fileBytesCopied = 0;

        reportProgress();
    }

    private boolean reportStartFile(File source, String targetPath) {
        progress.status = ProgressStatus.START_FILE;
        progress.sourcePath = source.getAbsolutePath();
        progress.targetPath = targetPath;
        progress.fileBytesAvail = source.length();
        progress.fileBytesCopied = 0;

        return reportProgress();
    }

    boolean reportCopyingFile(long addBytesCopied) {
        progress.status = ProgressStatus.COPYING_FILE;
        progress.fileBytesCopied += addBytesCopied;
        progress.totalBytesCopied += addBytesCopied;

        return reportProgress();
    }

    private void reportFinishedFile() {
        progress.status = ProgressStatus.FINISHED_FILE;
        progress.fileBytesCopied = progress.fileBytesAvail;

        reportProgress();
    }

    private void reportInitializeProgress(String sourcePath, long addTotalBytes) {
        progress.sourcePath = sourcePath;
        progress.totalBytesAvail += addTotalBytes;

        reportProgress();
    }

    private void reportFinished() {
        progress.status = ProgressStatus.FINISHED_FILE;
        progress.fileBytesAvail = 0;
        progress.fileBytesCopied = 0;
        progress.sourcePath = "";
        progress.targetPath = "";

        reportProgress();
    }

    private boolean exists(File file) throws ErrorInfo {
        try {
            return file.exists();
        } catch (SecurityException e) {
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), file.getAbsolutePath()), true);
        }
    }

    private boolean isDirectory(File file) throws ErrorInfo {
        try {
            return file.isDirectory();
        } catch (SecurityException e) {
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), file.getAbsolutePath()), true);
        }
    }

    private File[] listFiles(File source) {
        try {
            return filenameFilter == null
                    ? source.listFiles()
                    : source.listFiles(filenameFilter);
        } catch (SecurityException e) {
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), source.getAbsolutePath()), true);
        }
    }

    private void initializeCopy(File source) {
        reportInitializeProgress(source.getAbsolutePath(), 0);

        for (var file : listFiles(source)) {
            if (isDirectory(file))
                initializeCopy(file);
            else
                reportInitializeProgress(file.getAbsolutePath(), file.length());
        }
    }

    private void copyEntries(List<File> sourceFiles, File targetFile) throws ErrorInfo {
        sourceFiles.forEach(source -> {
            File target = new File(queryDestPath(source, targetFile));

            if (exists(target))
                verifyNonRecursive(source, target);

            progress.rootSourcePath = source.getAbsolutePath();

            if (isDirectory(source))
                copyDir(source, target.getAbsolutePath());
            else
                copyFile(source, target.getAbsolutePath());
        });
    }

    private String queryDestPath(File source, File target) {
        Path srcPath = Path.of(source.getAbsolutePath());
        Path dstPath = Path.of(target.getAbsolutePath(), source.getName());

        if (alternativeTargetPathFunction == null)
            return dstPath.toString();

        if (dstPath.equals(srcPath) && target.exists())
            return alternativeTargetPathFunction.alternativePath(dstPath.toString());

        return dstPath.toString();
    }

    @SuppressWarnings("DuplicatedCode")
    private void copyDir(File source, String targetPath) {
        if (!reportStartDir(source.getAbsolutePath(), targetPath))
            return;

        while (true) {
            try {
                if (!canWriteDir(source, new File(targetPath)))
                    return;

                copyDirNoQuery(source, targetPath);

                break;
            } catch (ErrorInfo info) {
                if (queryErrorAction == null || info.doNotQuery())
                    throw info;

                if (progress.ignoreAllErrors || progress.ignoredErrors.contains(info.getError()))
                    return;

                switch (queryErrorAction.query(info.getError(), info.getErrorDescription(), source, new File(targetPath))) {
                    case RETRY:
                        continue;

                    case IGNORE:
                        return;

                    case IGNORE_ALL_THIS:
                        progress.ignoredErrors.add(info.getError());
                        return;

                    case IGNORE_ALL:
                        progress.ignoreAllErrors = true;
                        return;

                    default:
                        throw new ErrorInfo(info.getError(), info.getErrorDescription(), false);
                }
            }
        }

        reportFinishedDir(source.getAbsolutePath(), targetPath);
    }

    private void copyDirNoQuery(File source, String targetPath) {
        for (var file : listFiles(source)) {
            targetPath = Path.of(targetPath, file.getName()).toString();

            if (isDirectory(file))
                copyDir(file, targetPath);
            else
                copyFile(file, targetPath);
        }

        copyAttributes(source, new File(targetPath));
    }

    @SuppressWarnings("DuplicatedCode")
    private void copyFile(File source, String targetPath) {
        if (!reportStartFile(source, targetPath))
            return;

        while (true) {
            try {
                if (!canWriteFile(source, new File(targetPath)))
                    return;

                copyFileNoQuery(source, targetPath);

                break;
            } catch (ErrorInfo info) {
                if (queryErrorAction == null || info.doNotQuery())
                    throw info;

                if (progress.ignoreAllErrors || progress.ignoredErrors.contains(info.getError()))
                    return;

                switch (queryErrorAction.query(info.getError(), info.getErrorDescription(), source, new File(targetPath))) {
                    case RETRY:
                        continue;

                    case IGNORE:
                        return;

                    case IGNORE_ALL_THIS:
                        progress.ignoredErrors.add(info.getError());
                        return;

                    case IGNORE_ALL:
                        progress.ignoreAllErrors = true;
                        return;

                    default:
                        throw new ErrorInfo(info.getError(), info.getErrorDescription(), false);
                }
            }
        }

        reportFinishedFile();
    }

    private void copyFileNoQuery(File source, String targetPath) {
        File orgTarget = new File(targetPath);
        File target = orgTarget;
        long totalBytesRead = 0;

        try {
            if (target.exists())
                target = new File(targetPath + "~");

            if (!target.createNewFile())
                throw new ErrorInfo(Error.CREATE_FILE, String.format(CoreMessages.get("cannotCreateFile"), target.getAbsolutePath()), true);

            FileOutputStream out = new FileOutputStream(target);
            FileInputStream in = new FileInputStream(source);

            int bytesRead;

            while (true) {
                try {
                    bytesRead = in.read(copyBuffer);
                } catch (IOException e) {
                    cleanup(target);
                    throw new ErrorInfo(Error.READ_FILE, String.format(CoreMessages.get("cannotReadFile"), source.getAbsolutePath()), true);
                }

                if (bytesRead == -1)
                    break;

                try {
                    out.write(copyBuffer, 0, bytesRead);
                } catch (IOException e) {
                    cleanup(target);
                    throw new ErrorInfo(Error.WRITE_FILE, String.format(CoreMessages.get("cannotWriteFile"), target.getAbsolutePath()), true);
                }

                totalBytesRead += bytesRead;

                if (!reportCopyingFile(bytesRead)) {
                    progress.totalBytesCopied -= totalBytesRead;
                    cleanup(target);

                    return;
                }
            }

            in.close();
            out.close();

            copyAttributes(source, target);

            cleanup(target, orgTarget);
        } catch (SecurityException e) {
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), target.getAbsolutePath()), true);
        } catch (IOException e) {
            cleanup(target);
            throw new ErrorInfo(Error.CREATE_FILE, String.format(CoreMessages.get("cannotCreateFile"), target.getAbsolutePath()), true);
        }
    }

    private void cleanup(File target, File orgTarget) {
        if (orgTarget == target)
            return;

        cleanup(orgTarget);

        try {
            if (!target.renameTo(orgTarget)) {
                cleanup(target);
                throw new ErrorInfo(Error.RENAME_FILE, String.format(CoreMessages.get("cannotRenameFile"), target.getAbsolutePath()), true);
            }
        } catch (SecurityException e) {
            cleanup(target);
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), target.getAbsolutePath()), true);
        }
    }

    private void cleanup(File target) {
        try {
            if (target.exists() && !target.delete())
                throw new ErrorInfo(Error.DELETE_FILE, String.format(CoreMessages.get("cannotDeleteFile"), target.getAbsolutePath()), true);
        } catch (SecurityException e) {
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), target.getAbsolutePath()), true);
        }
    }

    private void copyAttributes(File source, File target) {
        if (!target.setLastModified(source.lastModified()) || !target.setExecutable(source.canExecute())) {
            throw new ErrorInfo(Error.SET_ATTRIBUTES, String.format(CoreMessages.get("cannotSetAttributes"), target.getAbsolutePath()), true);
        }
    }

    private boolean canWriteDir(File source, File target) {
        try {
            if (!target.exists()) {
                if (!target.mkdirs())
                    throw new ErrorInfo(Error.CREATE_DIR, String.format(CoreMessages.get("cannotCreateDir"), target.getAbsolutePath()), true);

                return true;
            }
        } catch (SecurityException e) {
            throw new ErrorInfo(Error.ACCESS, String.format(CoreMessages.get("accessDenied"), target.getAbsolutePath()), true);
        }

        if (progress.dirOverwriteAction == OverwriteAction.NONE)
            return false;

        if (!isDirectory(target))
            throw new ErrorInfo(Error.TARGET_SOURCE_TYPE, String.format(CoreMessages.get("cannotOverwriteFileWithDir"), target.getAbsolutePath()), true);

        if (progress.dirOverwriteAction == OverwriteAction.ALL)
            return true;

        if (queryOverwriteAction == null)
            return false;

        progress.dirOverwriteAction = queryOverwriteAction.query(source, target);

        if (progress.dirOverwriteAction == null || progress.dirOverwriteAction == OverwriteAction.CANCEL)
            cancel();

        return progress.dirOverwriteAction == OverwriteAction.ALL || progress.dirOverwriteAction == OverwriteAction.ONE;
    }

    boolean canWriteFile(File source, File target) {
        if (!exists(target))
            return true;

        if (progress.fileOverwriteAction == OverwriteAction.NONE)
            return false;

        if (isDirectory(target))
            throw new ErrorInfo(Error.TARGET_SOURCE_TYPE, String.format(CoreMessages.get("cannotOverwriteDirWithFile"), target.getAbsolutePath()), true);

        if (progress.fileOverwriteAction == OverwriteAction.ALL)
            return true;

        if (queryOverwriteAction == null)
            return false;

        progress.fileOverwriteAction = queryOverwriteAction.query(source, target);

        if (progress.dirOverwriteAction == null || progress.fileOverwriteAction == OverwriteAction.CANCEL)
            cancel();

        return progress.fileOverwriteAction == OverwriteAction.ALL || progress.fileOverwriteAction == OverwriteAction.ONE;
    }

    private void cancel() throws ErrorInfo {
        throw new ErrorInfo(Error.CANCELED, CoreMessages.get("operationCanceled"), true);
    }
}
