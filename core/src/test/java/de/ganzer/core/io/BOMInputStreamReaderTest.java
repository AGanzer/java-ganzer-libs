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
        try (FileOutputStream fos = new FileOutputStream(cs.name());
             OutputStreamWriter osw = new OutputStreamWriter(fos, cs)
        ) {
            if (bom != null)
                fos.write(bom, 0, bom.length);

            osw.write(text);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static String read(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             BOMInputStreamReader isr = new BOMInputStreamReader(fis, StandardCharsets.ISO_8859_1)
        ) {
            BufferedReader bir = new BufferedReader(isr);
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
}
