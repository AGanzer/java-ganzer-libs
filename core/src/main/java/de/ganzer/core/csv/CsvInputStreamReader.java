package de.ganzer.core.csv;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.io.BOMInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads values from a CSV formatted stream.
 * <p>
 * This implementation covers RFC 4180 that can be found in the
 * <a href="https://www.rfc-archive.org/getrfc.php?rfc=4180#gsc.tab=0">RFC Archive</a>
 * or in the <a href="https://www.rfc-editor.org/rfc/rfc4180">RFC Editor</a>.
 */
@SuppressWarnings("unused")
public class CsvInputStreamReader extends BOMInputStreamReader {
    private char valueSeparator = ',';
    private char maskChar = '"';
    private boolean readEmptyLineAsEmptyValue;
    private int lastRead;
    private int currentLine = 1;
    private int currentColumn;
    private boolean skipRead;

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in) throws IOException {
        super(in);
    }

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in, String charsetName) throws IOException {
        super(in, charsetName);
    }

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in, Charset cs) throws IOException {
        super(in, cs);
    }

    /**
     * Gets the separator used for value separation.
     *
     * @return The set separator. The default is ','.
     */
    public char getValueSeparator() {
        return valueSeparator;
    }

    /**
     * Sets the separator to use for value separation.
     *
     * @param valueSeparator The separator to use.
     */
    public void setValueSeparator(char valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    /**
     * Gets the character to use for value masking.
     *
     * @return The used character. The default is '"'.
     */
    public char getMaskChar() {
        return maskChar;
    }

    /**
     * Sets the character to use for value masking.
     *
     * @param maskChar The character to use.
     */
    public void setMaskChar(char maskChar) {
        this.maskChar = maskChar;
    }

    /**
     * Indicates whether an empty line is treated as an empty value.
     * <p>
     * By default, empty lines are skipped, but there may be situations where
     * this is not wanted (single column files). In this case this property
     * can be changed to {@code true} to read empty values instead of skipping
     * empty lines.
     *
     * @return {@code true} if empty lines are treated as an empty value;
     * otherwise, {@code false} is returned.
     *
     * @see #setReadEmptyLineAsEmptyValue(boolean)
     */
    public boolean isReadEmptyLineAsEmptyValue() {
        return readEmptyLineAsEmptyValue;
    }

    /**
     * Sets a value that indicates whether an empty line is treated as an empty
     * value.
     * <p>
     * By default, empty lines are skipped, but there may be situations where
     * this is not wanted (single column files). In this case this property
     * can be changed to {@code true} to read empty values instead of skipping
     * empty lines.
     *
     * @param readEmptyLineAsEmptyValue {@code true} to treat empty lines as
     *                                   empty values.
     */
    public void setReadEmptyLineAsEmptyValue(boolean readEmptyLineAsEmptyValue) {
        this.readEmptyLineAsEmptyValue = readEmptyLineAsEmptyValue;
    }

    /**
     * Reads a single line from the input stream.
     * <p>
     * Empty lines are ignored.
     *
     * @return The values of the read line or an empty collection if there are
     * no more values to read.
     * @throws IOException         If an I/O error occurs.
     * @throws InvalidCsvException If the CSV stream is malformed.
     */
    public List<String> readLine() throws IOException, InvalidCsvException {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();

        do {
            while (readValue(value)) {
                values.add(value.toString());
                value.setLength(0);

                if (stopReading())
                    break;
            }

            checkEOL();
        } while (values.isEmpty() && lastRead != -1); // This skips empty lines!

        return values;
    }

    private boolean stopReading() {
        return lastRead == -1 || lastRead == '\r' || lastRead == '\n';
    }

    private boolean readValue(StringBuilder value) throws IOException, InvalidCsvException {
        ++currentColumn;

        if (skipRead)
            skipRead = false;
        else
            lastRead = read();

        if (lastRead == -1)
            return false;

        if (lastRead == maskChar)
            return readMaskedValue(value);

        if (stopReading())
            return isEmptyValue();

        if (lastRead == valueSeparator)
            return true;

        value.append((char)lastRead);

        return readUnmaskedValue(value);
    }

    private boolean isEmptyValue() {
        return currentColumn != 1 || readEmptyLineAsEmptyValue;
    }

    private boolean readMaskedValue(StringBuilder value) throws IOException, InvalidCsvException {
        while (true) {
            ++currentColumn;

            lastRead = read();

            if (endOfMaskedValueReached(lastRead))
                break;

            value.append((char)lastRead);
        }

        if (lastRead == -1)
            throw new InvalidCsvException(String.format(CoreMessages.get("unexpectedEndOfData"), currentLine, currentColumn));

        if (!endOfValueReached(lastRead))
            throw new InvalidCsvException(String.format(CoreMessages.get("separatorExpected"), currentLine, currentColumn));

        return !value.isEmpty();
    }

    private boolean endOfMaskedValueReached(int c) throws IOException {
        if (c == -1)
            return true;

        if (c != maskChar)
            return false;

        lastRead = read();

        return lastRead != maskChar;
    }

    private boolean readUnmaskedValue(StringBuilder value) throws IOException {
        while (true) {
            ++currentColumn;

            lastRead = read();

            if (endOfValueReached(lastRead))
                break;

            value.append((char)lastRead);
        }

        return !value.isEmpty();
    }

    private boolean endOfValueReached(int c) {
        return c == valueSeparator || c == '\n' || c == '\r' || c == -1;
    }

    private void checkEOL() throws IOException {
        if (lastRead == '\n') {
            countLine();
        } else if (lastRead == '\r') {
            countLine();

            if (lastRead == '\n')
                lastRead = read();
        }
    }

    private void countLine() throws IOException {
        ++currentLine;
        currentColumn = 0;

        lastRead = read();
        skipRead = true;
    }
}
