package de.ganzer.core.files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileNamesTest {
    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "ab.c, true",
            "ab/.c, false"})
    void isValidName(String name, String expected) {
        FileNames fn = new FileNames();
        assertEquals("true".equals(expected), fn.isValidName(name));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "a?b.c*, true",
            "a?b/.c*, false"})
    void isValidMaskedName(String name, String expected) {
        FileNames fn = new FileNames();
        assertEquals("true".equals(expected), fn.isValidName(name));
    }

    @Test
    void getValidName2() {
        FileNames fn = new FileNames(c -> "_");
        assertEquals("a_b.c", fn.getValidName("a/b.c"));
    }

    @Test
    void getValidMaskedName2() {
        FileNames fn = new FileNames(c -> "_");
        assertEquals("a?_b.c*", fn.getValidName("a?/b.c*"));
    }

    @Test
    void getValidName() {
        FileNames fn = new FileNames();
        assertEquals("a%2Fb.c", fn.getValidName("a/b.c"));
    }

    @Test
    void getValidMaskedName() {
        FileNames fn = new FileNames();
        assertEquals("a?%2Fb.c*", fn.getValidName("a?/b.c*"));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "name, name",
            "name.e1, name",
            "name.e1.e2, name.e1"})
    void getNameWithoutLastExtension(String name, String expected) {
        assertEquals(expected, FileNames.getNameWithoutLastExtension(name));
    }


    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "name, name",
            "name.e1, name",
            "name.e1.e2, name"})
    void getNameWithoutExtensions(String name, String expected) {
        assertEquals(expected, FileNames.getNameWithoutExtensions(name));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "name,",
            "name.e1, e1",
            "name.e1.e2, e2"})
    void getExtension(String name, String expected) {
        assertEquals(expected == null ? "" : expected, FileNames.getExtension(name));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "name,",
            "name.e1, e1",
            "name.e1.e2, e1.e2"})
    void getAllExtensions(String name, String expected) {
        assertEquals(expected == null ? "" : expected, FileNames.getAllExtensions(name));
    }

    @Test
    void getUniqueNameWithHint() {
        String existing = "./name.ext";
        String expected = "./name - Copy.ext";
        FileNames fn = new FileNames();

        try {
            createFile(existing);
            assertEquals(expected, fn.getUniqueName(existing, "Copy"));
        } finally {
            deleteFile(existing);
        }
    }

    @Test
    void getUniqueName2WithHint() {
        String existing1 = "./name.ext";
        String existing2 = "./name - Copy.ext";
        String expected = "./name - Copy (2).ext";
        FileNames fn = new FileNames();

        try {
            createFile(existing1);
            createFile(existing2);
            assertEquals(expected, fn.getUniqueName(existing1, "Copy"));
        } finally {
            deleteFile(existing2);
            deleteFile(existing1);
        }
    }

    @Test
    void getUniqueNameWithoutHint() {
        String existing = "./name.ext";
        String expected = "./name (2).ext";
        FileNames fn = new FileNames();

        try {
            createFile(existing);
            assertEquals(expected, fn.getUniqueName(existing, null));
        } finally {
            deleteFile(existing);
        }
    }

    @Test
    void getUniqueName2WithoutHint() {
        String existing1 = "./name.ext";
        String existing2 = "./name (2).ext";
        String expected = "./name (3).ext";
        FileNames fn = new FileNames();

        try {
            createFile(existing1);
            createFile(existing2);
            assertEquals(expected, fn.getUniqueName(existing1, null));
        } finally {
            deleteFile(existing2);
            deleteFile(existing1);
        }
    }

    private static void createFile(String path) {
        File file = new File(path);

        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write('.');
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFile(String path) {
        File file = new File(path);

        if (file.exists() && !file.delete())
            System.err.println("Could not delete file " + file.getAbsolutePath());
    }
}
