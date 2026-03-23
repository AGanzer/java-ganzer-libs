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
    private boolean eol;

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
     *
     * @throws IOException         If an I/O error occurs.
     * @throws InvalidCsvException If the CSV stream is malformed.
     */
    public List<String> readLine() throws IOException, InvalidCsvException {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();

        do {
            ++currentColumn;

            if (skipRead)
                skipRead = false;
            else
                lastRead = read();

            if (lastRead == -1)
                break;

            if (lastRead == maskChar) {
                readMaskedValue(value);

                values.add(value.toString());
                value.setLength(0);

                if (eol) {
                    skipAndCountLine();
                    break;
                }
            } else if (lastRead == valueSeparator) {
                values.add("");
            } else if (isEOL(lastRead)) {
                skipAndCountLine();

                if (values.isEmpty() && !readEmptyLineAsEmptyValue)
                    continue;

                values.add("");
                break;
            } else {
                value.append((char) lastRead);
                readUnmaskedValue(value);

                values.add(value.toString());
                value.setLength(0);

                if (eol) {
                    skipAndCountLine();
                    break;
                }
            }
        } while (lastRead != -1);

        return values;
    }

    private void readUnmaskedValue(StringBuilder value) throws IOException {
        while (true) {
            ++currentColumn;

            lastRead = read();

            if (lastRead == -1 || lastRead == valueSeparator)
                break;

            if (isEOL(lastRead)) {
                eol = true;
                break;
            }

            value.append((char) lastRead);
        }
    }

    private void readMaskedValue(StringBuilder value) throws IOException {
        while (true) {
            ++currentColumn;

            if (skipRead)
                skipRead = false;
            else
                lastRead = read();

            if (lastRead == -1)
                throw new InvalidCsvException(CoreMessages.get("unexpectedEndOfData", currentLine, currentColumn));

            if (lastRead != maskChar) {
                if (!isEOL(lastRead)) {
                    value.append((char) lastRead);
                } else {
                    countLine();

                    if (lastRead == '\n')
                        value.append('\n');
                    else if (lastRead == '\r') {
                        value.append('\r');

                        lastRead = read();

                        if (lastRead == '\n')
                            value.append('\n');
                        else
                            skipRead = true;
                    }
                }
            } else {
                lastRead = read();

                if (lastRead == maskChar) {
                    value.append(maskChar);
                } else {
                    if (isEOL(lastRead)) {
                        eol = true;
                        break;
                    }

                    if (lastRead == valueSeparator)
                        break;

                    throw new InvalidCsvException(CoreMessages.get("separatorExpected", currentLine, currentColumn));
                }
            }
        }
    }

    private void skipAndCountLine() throws IOException {
        skipLine();
        countLine();
    }

    private void skipLine() throws IOException {
        if (lastRead == '\n')
            lastRead = read();
        else if (lastRead == '\r') {
            lastRead = read();

            if (lastRead == '\n')
                lastRead = read();
        }

        skipRead = true;
    }

    private void countLine() {
        ++currentLine;
        currentColumn = 0;
        eol=false;
    }

    private boolean isEOL(int lastRead) {
        return lastRead == '\r' || lastRead == '\n';
    }
}
