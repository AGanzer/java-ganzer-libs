package de.ganzer.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {
    private enum TestValue {
        ONE,
        TWO
    }

    @Test
    void readDefaultInt() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        int value = 1;

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void readDefaultLong() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        long value = 1;

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void readDefaultFloat() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        float value = 1;

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void readDefaultDouble() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        double value = 1;

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void readDefaultString() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        String value = "1";

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void readDefaultBoolean() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        boolean value = true;

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void readDefaultEnum() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        TestValue value = TestValue.ONE;

        assertEquals(value, settings.read(key, value));
    }

    @Test
    void writeInt() {
        Settings settings = new Settings();
        String key = "test";
        int value = 1;

        settings.write(key, value);

        assertEquals(value, settings.read(key, 0));
    }

    @Test
    void writeLong() {
        Settings settings = new Settings();
        String key = "test";
        long value = 1;

        settings.write(key, value);

        assertEquals(value, settings.read(key, 0L));
    }

    @Test
    void writeFloat() {
        Settings settings = new Settings();
        String key = "test";
        float value = 1;

        settings.write(key, value);

        assertEquals(value, settings.read(key, .0f));
    }

    @Test
    void writeDouble() {
        Settings settings = new Settings();
        String key = "test";
        double value = 1;

        settings.write(key, value);

        assertEquals(value, settings.read(key, .0));
    }

    @Test
    void writeString() {
        Settings settings = new Settings();
        String key = "test";
        String value = "1";

        settings.write(key, value);

        assertEquals(value, settings.read(key, ""));
    }

    @Test
    void writeBoolean() {
        Settings settings = new Settings();
        String key = "test";
        boolean value = true;

        settings.write(key, value);

        assertEquals(value, settings.read(key, false));
    }

    @Test
    void writeEnum() {
        Settings settings = new Settings();
        String key = "test";
        TestValue value = TestValue.ONE;

        settings.write(key, value);

        assertEquals(value, settings.read(key, TestValue.TWO));
    }
}
