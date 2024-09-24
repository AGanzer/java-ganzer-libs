package de.ganzer.core.io;

import sun.nio.cs.StreamDecoder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A BOMInputStreamReader is a bridge from byte streams to character streams:
 * It reads bytes and decodes them into characters using a specified
 * {@link Charset charset}.
 * <p>
 * The charset that it uses is detected by interpreting the BOM if the input
 * stream starts with any; otherwise, a fallback charset is used.
 * <p>
 * Each invocation of one of an InputStreamReader's read() methods may
 * cause one or more bytes to be read from the underlying byte-input stream.
 * To enable the efficient conversion of bytes to characters, more bytes may
 * be read ahead from the underlying stream than are necessary to satisfy the
 * current read operation.
 * <p>
 * For top efficiency, consider wrapping an InputStreamReader within a
 * BufferedReader. For example:
 * <p>
 * <code>
 * BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 * </code>
 *
 * @see BufferedReader
 * @see InputStream
 * @see Charset
 */
public class BOMInputStreamReader extends Reader {
    private final PushbackInputStream in;
    private final StreamDecoder sd;

    /**
     * Creates an InputStreamReader that uses the default charset as fallback.
     *
     * @param in The input stream to read.
     *
     * @throws NullPointerException {@code in} is {@code null}.
     */
    public BOMInputStreamReader(InputStream in) throws IOException {
        super(in);
        this.in = new PushbackInputStream(in);
        this.sd = getDecoder(Charset.defaultCharset());
    }

    /**
     * Creates an InputStreamReader that uses the named charset as fallback.
     *
     * @param in The input stream to read.
     * @param charsetName The name of a supported {@link Charset charset} to use
     *        as fallback.
     *
     * @throws NullPointerException {@code in} is {@code null}.
     * @throws IllegalArgumentException {@code charsetName} is {@code null}.
     * @throws UnsupportedEncodingException If the named charset is not supported.
     */
    public BOMInputStreamReader(InputStream in, String charsetName) throws IOException, UnsupportedEncodingException {
        super(in);
        this.in = new PushbackInputStream(in);
        this.sd = getDecoder(Charset.forName(charsetName));
    }

    /**
     * Creates an InputStreamReader that uses the specified charset as fallback.
     *
     * @param in The input stream to read.
     * @param cs The charset to use as fallback.

     * @throws NullPointerException {@code in} is {@code null}.
     * @exception  IOException  If an I/O error occurs.
     */
    public BOMInputStreamReader(InputStream in, Charset cs) throws IOException {
        super(in);
        this.in = new PushbackInputStream(in);
        this.sd = getDecoder(cs);
    }

    /**
     * Returns the name of the character encoding being used by this stream.
     * <p>
     * If the encoding has an historical name then that name is returned;
     * otherwise, the encoding's canonical name is returned.
     * <p>
     * If this instance was created with the {@link #BOMInputStreamReader(InputStream, String)}
     * constructor then the returned name, being unique for the encoding, may
     * differ from the name passed to the constructor.
     *
     * @return The historical name of this encoding, or {@code null} if the stream
     *         has been closed.
     *
     * @see Charset
     */
    public String getEncoding() {
        return sd.getEncoding();
    }

    /**
     * Reads a single character.
     *
     * @return The character read, or -1 if the end of the stream has been
     *         reached.
     *
     * @exception  IOException  If an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        return sd.read();
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
     */
    @Override
    public int read(char[] buf, int off, int len) throws IOException {
        Objects.requireNonNull(buf, "buf must not be null.");
        return sd.read(buf, off, len);
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
     */
    @Override
    public boolean ready() throws IOException {
        return sd.ready();
    }

    /**
     * Closes this reader.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        sd.close();
    }

    private enum BOM {
        UTF_32_BE(new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF}),
        UTF_32_LE(new byte[] {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00}),
        UTF_8(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}),
        UTF_16_BE(new byte[] {(byte) 0xFE, (byte) 0xFF}),
        UTF_16_LE(new byte[] {(byte) 0xFF, (byte) 0xFE}),
        NONE(new byte[] {});

        private final byte[] bytes;

        BOM(byte[] bomBytes) {
            this.bytes = bomBytes;
        }

        public int getNumBytes() {
            return bytes.length;
        }

        public static BOM getBOM(byte[] bytes) {
            for (BOM bom : BOM.values()) {
                if (startsWith(bytes, bom.bytes))
                    return bom;
            }

            return NONE;
        }

        private static boolean startsWith(byte[] bytes, byte[] bom) {
            if (bom.length > bytes.length)
                return false;

            for (int i = 0; i < bom.length; i++) {
                if (bytes[i] != bom[i])
                    return false;
            }

            return true;
        }
    }

    private BOM getBOM() throws IOException {
        byte[] buff = new byte[4];
        int read = in.read(buff, 0, 4);

        if (read == -1)
            return BOM.NONE;

        BOM bom = BOM.getBOM(buff);
        int writeBack = read - bom.getNumBytes();

        if (writeBack > 0)
            in.unread(buff, bom.getNumBytes(), writeBack);

        return bom;
    }

    private StreamDecoder getDecoder(Charset cs) throws IOException {
        Objects.requireNonNull(cs, "Charset must not be null");

        Charset set = switch (getBOM()) {
            case UTF_32_BE -> Charset.forName("UTF-32BE");
            case UTF_32_LE -> Charset.forName("UTF-32LE");
            case UTF_8 -> StandardCharsets.UTF_8;
            case UTF_16_BE -> StandardCharsets.UTF_16BE;
            case UTF_16_LE -> StandardCharsets.UTF_16LE;
            default -> cs;
        };

        return StreamDecoder.forInputStreamReader(in, this, set);
    }
}
