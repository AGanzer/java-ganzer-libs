package de.ganzer.core.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class BOMInputStreamReaderTest {
    private static final String text = "ÄÖÜäöüß123";

    @BeforeAll
    static void setUpBeforeClass() {
        writeASCII();
        writeUTF8BOM();
        writeUTF16BEBOM();
        writeUTF16LEBOM();
        writeUTF32BEBOM();
        writeUTF32LEBOM();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterAll
    static void tearDownAfterClass() {
        new File(StandardCharsets.ISO_8859_1.name()).delete();
        new File(StandardCharsets.UTF_8.name()).delete();
        new File(StandardCharsets.UTF_16BE.name()).delete();
        new File(StandardCharsets.UTF_16LE.name()).delete();
        new File(Charset.forName("UTF_32BE").name()).delete();
        new File(Charset.forName("UTF_32LE").name()).delete();
    }

    private static void writeASCII() {
        write(StandardCharsets.ISO_8859_1, null);
    }

    private static void writeUTF8BOM() {
        write(StandardCharsets.UTF_8, new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
    }

    private static void writeUTF16BEBOM() {
        write(StandardCharsets.UTF_16BE, new byte[] {(byte) 0xFE, (byte) 0xFF});
    }

    private static void writeUTF16LEBOM() {
        write(StandardCharsets.UTF_16LE, new byte[] {(byte) 0xFF, (byte) 0xFE});
    }

    private static void writeUTF32BEBOM() {
        write(Charset.forName("UTF_32BE"), new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
    }

    private static void writeUTF32LEBOM() {
        write(Charset.forName("UTF_32LE"), new byte[] {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
    }

    private static void write(Charset cs, byte[] bom) {
        try (var fos = new FileOutputStream(cs.name());
             var osw = new OutputStreamWriter(fos, cs)
        ) {
            if (bom != null)
                fos.write(bom, 0, bom.length);

            osw.write(text);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static String read(String filename) {
        try (var fis = new FileInputStream(filename);
             var isr = new BOMInputStreamReader(fis, StandardCharsets.ISO_8859_1)
        ) {
            var bir = new BufferedReader(isr);
            return bir.readLine();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return "";
    }

    @Test
    void readASCII() {
        String read = read(StandardCharsets.ISO_8859_1.name());
        assertEquals(text, read);
    }

    @Test
    void readUTF8() {
        String read = read(StandardCharsets.UTF_8.name());
        assertEquals(text, read);
    }

    @Test
    void readUTF16BE() {
        String read = read(StandardCharsets.UTF_16BE.name());
        assertEquals(text, read);
    }

    @Test
    void readUTF16LE() {
        String read = read(StandardCharsets.UTF_16LE.name());
        assertEquals(text, read);
    }

    @Test
    void readUTF32BE() {
        String read = read(Charset.forName("UTF_32BE").name());
        assertEquals(text, read);
    }

    @Test
    void readUTF32LE() {
        String read = read(Charset.forName("UTF_32LE").name());
        assertEquals(text, read);
    }

    @Test
    void testReadChar() throws IOException {
        byte[] bytes = "ABC".getBytes(StandardCharsets.UTF_8);
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8)) {
            assertEquals('A', isr.readChar());
            assertEquals('B', isr.readChar());
            assertEquals('C', isr.readChar());
            assertThrows(EOFException.class, isr::readChar);
        }
    }

    @Test
    void testReadCharWithBOM() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        baos.write("ÄÖ".getBytes(StandardCharsets.UTF_8));
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.ISO_8859_1)) {
            assertEquals('Ä', isr.readChar());
            assertEquals('Ö', isr.readChar());
            assertThrows(EOFException.class, isr::readChar);
        }
    }

    @Test
    void testReadLineLF() throws IOException {
        String content = "Line 1\nLine 2\n\nLine 4";
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("Line 1", isr.readLine());
            assertEquals("Line 2", isr.readLine());
            assertEquals("", isr.readLine());
            assertEquals("Line 4", isr.readLine());
            assertNull(isr.readLine());
        }
    }

    @Test
    void testReadLineCRLF() throws IOException {
        String content = "Line 1\r\nLine 2\r\n\r\nLine 4\r\n";
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("Line 1", isr.readLine());
            assertEquals("Line 2", isr.readLine());
            assertEquals("", isr.readLine());
            assertEquals("Line 4", isr.readLine());
            assertNull(isr.readLine());
        }
    }

    @Test
    void testReadLineCR() throws IOException {
        String content = "Line 1\rLine 2\r\rLine 4";
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("Line 1", isr.readLine());
            assertEquals("Line 2", isr.readLine());
            assertEquals("", isr.readLine());
            assertEquals("Line 4", isr.readLine());
            assertNull(isr.readLine());
        }
    }

    @Test
    void testReadLineEmptyAndSingleLines() throws IOException {
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(new byte[0]), StandardCharsets.UTF_8)) {
            assertNull(isr.readLine());
        }

        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("", isr.readLine());
            assertNull(isr.readLine());
        }

        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream("\r\n".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("", isr.readLine());
            assertNull(isr.readLine());
        }

        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream("\r".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("", isr.readLine());
            assertNull(isr.readLine());
        }

        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream("Single Line".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals("Single Line", isr.readLine());
            assertNull(isr.readLine());
        }
    }

    @Test
    void testReadAllKeepWindowsCRLF() throws IOException {
        String content = "First line\r\nSecond line\r\nThird line\nFourth line\r";
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            String result = isr.readAll(true);
            assertEquals("First line\r\nSecond line\r\nThird line\nFourth line\r", result);
        }

        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(new byte[0]), StandardCharsets.UTF_8)) {
            assertEquals("", isr.readAll(true));
        }
    }

    @Test
    void testReadAllConvertWindowsCRLF() throws IOException {
        String content = "First line\r\nSecond line\r\nThird line\nFourth line\r";
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            String result = isr.readAll(false);
            assertEquals("First line\nSecond line\nThird line\nFourth line\r", result);
        }

        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(new byte[0]), StandardCharsets.UTF_8)) {
            assertEquals("", isr.readAll(false));
        }
    }

    @Test
    void testMixedReadOperations() throws IOException {
        String content = "A\r\nBC\rDEF\nG";
        try (var isr = new BOMInputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            assertEquals('A', isr.readChar());
            assertEquals("", isr.readLine()); // consumes the remaining \r\n after 'A'
            assertEquals("BC", isr.readLine());
            assertEquals('D', isr.readChar());
            assertEquals("EF\nG", isr.readAll(false));
        }
    }
}
