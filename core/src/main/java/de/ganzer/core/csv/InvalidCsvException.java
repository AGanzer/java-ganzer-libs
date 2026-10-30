package de.ganzer.core.csv;

/**
 * An instance of this is thrown when a miss formatted CSV file is read by
 * {@link CsvInputStreamReader}.
 *
 * @deprecated since 6.0.0 User {@link de.ganzer.core.io.CsvInputStreamReader}
 * instead.
 */
@Deprecated(forRemoval = true, since = "6.0.0")
public class InvalidCsvException extends RuntimeException {
    /**
     * {@inheritDoc}
     */
    public InvalidCsvException(String message) {
        super(message);
    }
}
