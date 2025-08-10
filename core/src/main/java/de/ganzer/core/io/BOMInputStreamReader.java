package de.ganzer.core.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.*;
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
 * For top efficiency, consider wrapping a BOMInputStreamReader within a
 * BufferedReader. For example:
 * <p>
 * <code>
 * BufferedReader in = new BufferedReader(new BOMInputStreamReader(System.in));
 * </code>
 *
 * @see BufferedReader
 * @see InputStream
 * @see Charset
 */
@SuppressWarnings("unused")
public class BOMInputStreamReader extends Reader {
    private final PushbackInputStream in;
    private final StreamDecoder sd;

    /**
     * Creates an InputStreamReader that uses the default charset as fallback.
     *
     * @param in The input stream to read.
     *
     * @throws NullPointerException {@code in} is {@code null}.
     * @throws IOException If an I/O error occurs.
     */
    public BOMInputStreamReader(InputStream in) throws IOException {
        super(in);
        this.in = new PushbackInputStream(in, 4);
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
     * @throws IOException If an I/O error occurs.
     */
    public BOMInputStreamReader(InputStream in, String charsetName) throws IOException {
        super(in);
        this.in = new PushbackInputStream(in, 4);
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
        this.in = new PushbackInputStream(in, 4);
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

        if (read < 1)
            return BOM.NONE;

        BOM bom = BOM.getBOM(buff);
        int bytesLeft = read - bom.getNumBytes();

        if (bytesLeft > 0)
            in.unread(buff, bom.getNumBytes(), bytesLeft);

        return bom;
    }

    private StreamDecoder getDecoder(Charset cs) throws IOException {
        Objects.requireNonNull(cs, "Charset must not be null");

        Charset set = switch (getBOM()) {
            case UTF_32_BE -> Charset.forName("UTF_32BE");
            case UTF_32_LE -> Charset.forName("UTF_32LE");
            case UTF_8 -> StandardCharsets.UTF_8;
            case UTF_16_BE -> StandardCharsets.UTF_16BE;
            case UTF_16_LE -> StandardCharsets.UTF_16LE;
            default -> cs;
        };

        return StreamDecoder.forInputStreamReader(in, this, set);
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    private static class StreamDecoder extends Reader {
        public static StreamDecoder forInputStreamReader(InputStream in, Object lock, String charsetName) throws UnsupportedEncodingException {
            String csn = charsetName;

            if (csn == null) {
                csn = Charset.defaultCharset().name();
            }

            try {
                return new StreamDecoder(in, lock, Charset.forName(csn));
            } catch (IllegalCharsetNameException | UnsupportedCharsetException x) {
                throw new UnsupportedEncodingException (csn);
            }
        }

        public static StreamDecoder forInputStreamReader(InputStream in, Object lock, Charset cs) {
            return new StreamDecoder(in, lock, cs);
        }

        public static StreamDecoder forInputStreamReader(InputStream in, Object lock, CharsetDecoder dec) {
            return new StreamDecoder(in, lock, dec);
        }

        public static StreamDecoder forDecoder(ReadableByteChannel ch, CharsetDecoder dec, int minBufferCap) {
            return new StreamDecoder(ch, dec, minBufferCap);
        }

        public String getEncoding() {
            if (isOpen())
                return encodingName();

            return null;
        }

        @Override
        public int read() throws IOException {
            return read0();
        }

        @Override
        public int read(char[] buf, int offset, int length) throws IOException {
            int off = offset;
            int len = length;

            synchronized (lock) {
                ensureOpen();
                if ((off < 0) || (off > buf.length) || (len < 0) || ((off + len) > buf.length) || ((off + len) < 0)) {
                    throw new IndexOutOfBoundsException();
                }

                if (len == 0)
                    return 0;

                int n = 0;

                if (haveLeftoverChar) {
                    // Copy the leftover char into the buffer:
                    //
                    buf[off] = leftoverChar;
                    off++; len--;
                    haveLeftoverChar = false;
                    n = 1;

                    if ((len == 0) || !implReady())
                        // Return now if this is all we can produce w/o blocking:
                        //
                        return n;
                }

                if (len == 1) {
                    // Treat single-character array reads just like read():
                    //
                    int c = read0();

                    if (c == -1)
                        return (n == 0) ? -1 : n;

                    buf[off] = (char)c;

                    return n + 1;
                }

                return n + implRead(buf, off, off + len);
            }
        }

        @Override
        public boolean ready() throws IOException {
            synchronized (lock) {
                ensureOpen();
                return haveLeftoverChar || implReady();
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (lock) {
                if (closed)
                    return;

                try {
                    implClose();
                } finally {
                    closed = true;
                }
            }
        }

        private static final int MIN_BYTE_BUFFER_SIZE = 32;
        private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;

        private volatile boolean closed;

        private final Charset cs;
        private final CharsetDecoder decoder;
        private final ByteBuffer bb;

        private final InputStream in;
        private final ReadableByteChannel ch;

        private StreamDecoder(InputStream in, Object lock, Charset cs) {
            this(in, lock, cs.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
        }

        private StreamDecoder(InputStream in, Object lock, CharsetDecoder dec) {
            super(lock);
            this.cs = dec.charset();
            this.decoder = dec;
            this.in = in;
            this.ch = null;
            bb = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
            bb.flip(); // So that bb is initially empty.
        }

        private StreamDecoder(ReadableByteChannel ch, CharsetDecoder dec, int mbc) {
            this.in = null;
            this.ch = ch;
            this.decoder = dec;
            this.cs = dec.charset();
            this.bb = ByteBuffer.allocate(mbc < 0
                    ? DEFAULT_BYTE_BUFFER_SIZE
                    : (Math.max(mbc, MIN_BYTE_BUFFER_SIZE)));
            bb.flip();
        }

        private void ensureOpen() throws IOException {
            if (closed)
                throw new IOException("Stream closed");
        }

        // In order to handle surrogates properly we must never try to produce
        // fewer than two characters at a time.  If we're only asked to return one
        // character then the other is saved here to be returned later:
        //
        private boolean haveLeftoverChar = false;
        private char leftoverChar;

        private int read0() throws IOException {
            synchronized (lock) {
                // Return the leftover char, if there is one:
                //
                if (haveLeftoverChar) {
                    haveLeftoverChar = false;
                    return leftoverChar;
                }

                // Convert more bytes:
                //
                char[] cb = new char[2];
                int n = read(cb, 0, 2);
                switch (n) {
                    case -1:
                        return -1;
                    case 2:
                        leftoverChar = cb[1];
                        haveLeftoverChar = true;
                        // FALL THROUGH!
                    case 1:
                        return cb[0];
                    default:
                        assert false : n;
                        return -1;
                }
            }
        }

        private boolean isOpen() {
            return !closed;
        }

        private int readBytes() throws IOException {
            bb.compact();
            try {
                if (ch != null) {
                    // Read from the channel:
                    //
                    int n = ch.read(bb);

                    if (n < 0)
                        return n;
                } else {
                    // Read from the input stream, and then update the buffer:
                    //
                    int lim = bb.limit();
                    int pos = bb.position();

                    assert (pos <= lim);

                    int rem = lim - pos;
                    int n = in.read(bb.array(), bb.arrayOffset() + pos, rem);

                    if (n < 0)
                        return n;

                    if (n == 0)
                        throw new IOException("Underlying input stream returned zero bytes");

                    assert (n <= rem) : "n = " + n + ", rem = " + rem;

                    bb.position(pos + n);
                }
            } finally {
                // Flip even when an IOException is thrown,
                // otherwise the stream will stutter:
                //
                bb.flip();
            }

            int rem = bb.remaining();
            assert (rem != 0) : rem;
            return rem;
        }

        int implRead(char[] cbuf, int off, int end) throws IOException {
            // In order to handle surrogate pairs, this method requires that
            // the invoker attempt to read at least two characters. Saving the
            // extra character, if any, at a higher level is easier than trying
            // to deal with it here.

            assert (end - off > 1);

            CharBuffer cb = CharBuffer.wrap(cbuf, off, end - off);
            if (cb.position() != 0) {
                // Ensure that cb[0] == buf[off]:
                //
                cb = cb.slice();
            }

            boolean eof = false;

            for (;;) {
                CoderResult cr = decoder.decode(bb, cb, eof);

                if (cr.isUnderflow()) {
                    if (eof)
                        break;

                    if (!cb.hasRemaining())
                        break;

                    if ((cb.position() > 0) && !inReady())
                        break; // Block at most once.

                    int n = readBytes();

                    if (n < 0) {
                        eof = true;

                        if ((cb.position() == 0) && (!bb.hasRemaining()))
                            break;

                        decoder.reset();
                    }

                    continue;
                }

                if (cr.isOverflow()) {
                    assert cb.position() > 0;
                    break;
                }

                cr.throwException();
            }

            if (eof) {
                // Need to flush decoder:
                //
                decoder.reset();
            }

            if (cb.position() == 0) {
                if (eof) {
                    return -1;
                }

                assert false;
            }

            return cb.position();
        }

        String encodingName() {
            return cs.name();
        }

        private boolean inReady() {
            try {
                return (((in != null) && (in.available() > 0)) || (ch instanceof FileChannel)); // RBC.available()?
            } catch (IOException x) {
                return false;
            }
        }

        boolean implReady() {
            return bb.hasRemaining() || inReady();
        }

        void implClose() throws IOException {
            if (ch != null) {
                ch.close();
            } else {
                in.close();
            }
        }
    }
}
