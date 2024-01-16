package de.ganzer.core.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
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
public class CsvInputStreamReader extends InputStreamReader {
    private char valueDelimiter = ',';
    private char maskChar = '"';
    private int lastRead;

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in) {
        super(in);
    }

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in, String charsetName) throws UnsupportedEncodingException {
        super(in, charsetName);
    }

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in, Charset cs) {
        super(in, cs);
    }

    /**
     * {@inheritDoc}
     */
    public CsvInputStreamReader(InputStream in, CharsetDecoder dec) {
        super(in, dec);
    }

    /**
     * Gets the delimiter used for value separation.
     *
     * @return The set delimiter. The default is ','.
     */
    public char getValueDelimiter() {
        return valueDelimiter;
    }

    /**
     * Sets the delimiter to use for value separation.
     *
     * @param valueDelimiter The delimiter to use.
     */
    public void setValueDelimiter(char valueDelimiter) {
        this.valueDelimiter = valueDelimiter;
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

        do {
            StringBuilder value = new StringBuilder();

            while (readValue(value)) {
                values.add(value.toString());
                value.setLength(0);

                if (stopReading())
                    break;
            }
        } while (values.isEmpty() && lastRead != -1); // Skip empty lines!

        return values;
    }

    private boolean stopReading() {
        return lastRead == -1 || lastRead == '\r' || lastRead == '\n';
    }

    private boolean readValue(StringBuilder value) throws IOException, InvalidCsvException {
        lastRead = read();

        if (lastRead == -1)
            return false;

        if (lastRead == maskChar)
            return readMaskedValue(value);

        if (stopReading() )
            return false;

        value.append((char)lastRead);

        return readUnmaskedValue(value);
    }

    private boolean readMaskedValue(StringBuilder value) throws IOException, InvalidCsvException {
        for (; ; ) {
            lastRead = read();

            if (endOfMaskedValueReached(lastRead))
                break;

            value.append((char)lastRead);
        }

        if (lastRead == -1)
            throw new InvalidCsvException("Unexpected end of data.");

        if (!endOfValueReached(lastRead))
            throw new InvalidCsvException("Delimiter expected.");

        return value.length() > 0;
    }

    private boolean endOfMaskedValueReached(int c) throws IOException, InvalidCsvException {
        if (c == -1)
            return true;

        if (c != maskChar)
            return false;

        lastRead = read();

        return lastRead != maskChar;
    }

    private boolean readUnmaskedValue(StringBuilder value) throws IOException {
        for (; ; ) {
            lastRead = read();

            if (endOfValueReached(lastRead))
                break;

            value.append((char)lastRead);
        }

        return value.length() > 0;
    }

    private boolean endOfValueReached(int c) {
        return c == valueDelimiter || c == '\n' || c == '\r' || c == -1;
    }
}
