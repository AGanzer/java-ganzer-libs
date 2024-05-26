package de.ganzer.core.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Collection;
import java.util.Objects;

/**
 * Writes values as formatted CSV into a stream.
 * <p>
 * This implementation covers RFC 4180 that can be found in the
 * <a href="https://www.rfc-archive.org/getrfc.php?rfc=4180#gsc.tab=0">RFC Archive</a>
 * or in the <a href="https://www.rfc-editor.org/rfc/rfc4180">RFC Editor</a>.
 */
@SuppressWarnings("unused")
public class CsvOutputStreamWriter extends OutputStreamWriter {
    private String lineSeparator = System.lineSeparator();
    private char valueSeparator = ',';
    private char maskChar = '"';

    /**
     * {@inheritDoc}
     */
    public CsvOutputStreamWriter(OutputStream out) {
        super(out);
    }

    /**
     * {@inheritDoc}
     */
    public CsvOutputStreamWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException {
        super(out, charsetName);
    }

    /**
     * {@inheritDoc}
     */
    public CsvOutputStreamWriter(OutputStream out, Charset cs) {
        super(out, cs);
    }

    /**
     * {@inheritDoc}
     */
    public CsvOutputStreamWriter(OutputStream out, CharsetEncoder enc) {
        super(out, enc);
    }

    /**
     * Gets the separator used for line separation.
     *
     * @return The set separator. The default is the system's line separator.
     */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * Sets the separator to use for line separation.
     *
     * @param lineSeparator The separator to use. If this is {@code null} or
     *                      empty, the system's line separator is used.
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator == null || lineSeparator.isEmpty()
                ? System.lineSeparator()
                : lineSeparator;
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
     * Writes one line into the target.
     * <p>
     * This simply calls <code>writeLine(values, false)</code>.
     *
     * @param values The values to write. This will automatically be masked and seperated
     *               by the set value delimiter. The set line delimiter is appended after
     *               all values are written.
     *
     * @throws IOException If an I/O error occurs.
     * @throws NullPointerException values is {@code null}.
     *
     * @see #getLineSeparator()
     * @see #getValueSeparator()
     * @see #setLineSeparator(String)
     * @see #setValueSeparator(char)
     * @see #writeLine(Collection, boolean)
     */
    public void writeLine(Collection<String> values) throws IOException {
        writeLine(values, false);
    }

    /**
     * Writes one line into the target.
     *
     * @param values     The values to write. This will automatically be masked and seperated
     *                   by the set value delimiter. The set line delimiter is appended after
     *                   all values are written. This must not be {@code null}.
     * @param maskAlways If <code>true</code>, the values will always be masked.
     *
     * @throws IOException If an I/O error occurs.
     * @throws NullPointerException values is {@code null}.
     *
     * @see #getLineSeparator()
     * @see #getValueSeparator()
     * @see #setLineSeparator(String)
     * @see #setValueSeparator(char)
     */
    public void writeLine(Collection<String> values, boolean maskAlways) throws IOException {
        Objects.requireNonNull(values, "CsvOutputStreamWriter::writeLine: values");

        boolean separate = false;

        for (String value : values) {
            if (separate)
                write(valueSeparator);
            else
                separate = true;

            writeValue(value, maskAlways);
        }

        write(lineSeparator);
    }

    private void writeValue(String value, boolean maskAlways) throws IOException {
        if (maskAlways || shouldMask(value))
            writeMaskedValue(value);
        else
            writeUnmaskedValue(value);
    }

    private boolean shouldMask(String value) {
        for (int i = 0; i < value.length(); ++i) {
            if (requiresMask(value.charAt(i)))
                return true;
        }

        return false;
    }

    private boolean requiresMask(char c) {
        return c == maskChar || c == valueSeparator || c == '\n' || c == '\r';
    }

    private void writeMaskedValue(String value) throws IOException {
        write(maskChar);

        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);

            if (c == maskChar)
                write(maskChar);

            write(c);
        }

        write(maskChar);
    }

    private void writeUnmaskedValue(String value) throws IOException {
        write(value);
    }
}
