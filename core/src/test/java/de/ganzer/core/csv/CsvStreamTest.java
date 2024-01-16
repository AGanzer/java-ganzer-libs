package de.ganzer.core.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvStreamTest {
    @Test
    void testEmptyReadWrite() throws IOException {
        String csvContent = "";
        String writtenContent = doReadWrite(csvContent, false, "\n");

        Assertions.assertEquals(csvContent, writtenContent);
    }

    @Test
    void testFullReadWrite() throws IOException {
        String csvContent = "1,2,3\n" +
                "\"1,1\",\"2\n2\",3\n" +
                "1,2,\"3\"\"3\"\n";
        String writtenContent = doReadWrite(csvContent, false, "\n");

        Assertions.assertEquals(csvContent, writtenContent);
    }

    @Test
    void testFullReadWriteWindowsLineFeed() throws IOException {
        String csvContent = "1,2,3\r\n" +
                "\"1,1\",\"2\r\n2\",3\r\n" +
                "1,2,\"3\"\"3\"\r\n";
        String writtenContent = doReadWrite(csvContent, false, "\r\n");

        Assertions.assertEquals(csvContent, writtenContent);
    }

    @Test
    void testFullReadWriteNoEol() throws IOException {
        String csvContent = "1,2,3\n" +
                "\"1,1\",\"2\n2\",3\n" +
                "1,2,3";
        String writtenContent = doReadWrite(csvContent, false, "\n");

        Assertions.assertEquals(csvContent + "\n", writtenContent);
    }

    @Test
    void testFullReadWriteMaskAll() throws IOException {
        String csvContent = "1,2,3\n" +
                "\"1,1\",\"2\n2\",3\n" +
                "1,2,\"3\"\"3\"\n";
        String expectedContent = "\"1\",\"2\",\"3\"\n" +
                "\"1,1\",\"2\n2\",\"3\"\n" +
                "\"1\",\"2\",\"3\"\"3\"\n";
        String writtenContent = doReadWrite(csvContent, true, "\n");

        Assertions.assertEquals(expectedContent, writtenContent);
    }

    @Test
    void testThrowUnexpectedEnd() {
        String csvContent = "1,2,3\n" +
                "\"1,1\",\"2\n2\",3\n" +
                "1,2,\"3\"\"3\n";

        Assertions.assertThrows(
                InvalidCsvException.class,
                () -> doReadWrite(csvContent, false, "\n"),
                "InvalidCsvException not thrown.");
    }

    @Test
    void testThrowDelimiterExpected() {
        String csvContent = "1,2,3\n" +
                "\"1,1\"a,\"2\n2\",3\n" +
                "1,2,\"3\"\"3\"\n";

        Assertions.assertThrows(
                InvalidCsvException.class,
                () -> doReadWrite(csvContent, false, "\n"),
                "InvalidCsvException not thrown.");
    }

    private String doReadWrite(String csvContent, boolean maskAlways, String lineFeed) throws IOException {
        InputStream is = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        CsvInputStreamReader r = new CsvInputStreamReader(is, StandardCharsets.UTF_8);

        List<List<String>> readContent = new ArrayList<>();

        for (; ; ) {
            List<String> line = r.readLine();

            if (line.isEmpty())
                break;

            readContent.add(line);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        CsvOutputStreamWriter w = new CsvOutputStreamWriter(os, StandardCharsets.UTF_8);
        w.setLineDelimiter(lineFeed);

        for (List<String> line : readContent)
            w.writeLine(line, maskAlways);

        w.flush();

        return os.toString(StandardCharsets.UTF_8);
    }
}
