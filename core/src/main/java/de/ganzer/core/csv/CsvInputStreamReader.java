package de.ganzer.core.csv;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.io.BOMInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
public class CsvInputStreamReader extends Reader {
    private final BOMInputStreamReader reader;
    private char valueSeparator = ',';
    private char maskChar = '"';
    private boolean readEmptyLineAsEmptyValue;
    private int lastRead;
    private int currentLine = 1;
    private int currentColumn;
    private boolean skipRead;
    private boolean eol;

    /**
     * Creates a new CsvInputStreamReader.
     *
     * @param in The input stream to read.
     * @throws IOException If an I/O error occurs.
     */
    public CsvInputStreamReader(InputStream in) throws IOException {
        super(in);
        this.reader = new BOMInputStreamReader(in);
    }

    /**
     * Creates a new CsvInputStreamReader.
     *
     * @param in The input stream to read.
     * @param charsetName The name of a supported charset to use as fallback.
     * @throws IOException If an I/O error occurs.
     */
    public CsvInputStreamReader(InputStream in, String charsetName) throws IOException {
        super(in);
        this.reader = new BOMInputStreamReader(in, charsetName);
    }

    /**
     * Creates a new CsvInputStreamReader.
     *
     * @param in The input stream to read.
     * @param cs The charset to use as fallback.
     * @throws IOException If an I/O error occurs.
     */
    public CsvInputStreamReader(InputStream in, Charset cs) throws IOException {
        super(in);
        this.reader = new BOMInputStreamReader(in, cs);
    }

    /**
     * Gets the input stream the reader works on.
     *
     * @return The input stream. This is not the same as the one that is set at
     *         construction.
     *
     * @since 6.0.0
     */
    public InputStream getInputStream() {
        return reader.getInputStream();
    }

    /**
     * Returns the name of the character encoding being used by this stream.
     *
     * @return The historical name of this encoding, or {@code null} if the stream
     *         has been closed.
     *
     * @since 6.0.0
     */
    public String getEncoding() {
        return reader.getEncoding();
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
     * Reads a single character.
     *
     * @return The character read, or -1 if the end of the stream has been
     *         reached.
     *
     * @exception  IOException  If an I/O error occurs.
     *
     * @since 6.0.0
     */
    @Override
    public int read() throws IOException {
        return reader.read();
    }

    /**
     * Reads characters into a portion of an array.
     *
     * @param buf The destination buffer.
     * @param off Offset at which to start storing characters
     * @param len Maximum number of characters to read
     *
     * @return The number of characters read, or -1 if the end of the stream has
     *         been reached
     *
     * @throws IOException If an I/O error occurs.
     * @throws NullPointerException {@code buf} is {@code null}.
     *
     * @since 6.0.0
     */
    @Override
    public int read(char[] buf, int off, int len) throws IOException {
        return reader.read(buf, off, len);
    }

    /**
     * Tells whether this stream is ready to be read.
     * <p>
     * An input stream reader is ready if its input buffer is not empty, or if
     * bytes are available to be read from the underlying byte stream.
     *
     * @return {@code true} if the reader is ready; otherwise, {@code false}.
     *
     * @throws IOException  If an I/O error occurs.
     *
     * @since 6.0.0
     */
    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    /**
     * Closes this reader.
     *
     * @throws IOException If an I/O error occurs.
     *
     * @since 6.0.0
     */
    @Override
    public void close() throws IOException {
        reader.close();
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
