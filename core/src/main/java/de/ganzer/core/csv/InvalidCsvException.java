package de.ganzer.core.csv;

/**
 * An instance of this is thrown when a miss formatted CSV file is read by
 * {@link CsvInputStreamReader}.
 */
public class InvalidCsvException extends RuntimeException {
    /**
     * {@inheritDoc}
     */
    public InvalidCsvException(String message) {
        super(message);
    }
}
