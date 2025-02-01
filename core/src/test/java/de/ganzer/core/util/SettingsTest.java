package de.ganzer.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {
    private enum TestValue {
        ONE,
        TWO
    }

    @Test
    void getDefaultInt() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        int value = 1;

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void getDefaultLong() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        long value = 1;

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void getDefaultFloat() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        float value = 1;

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void getDefaultDouble() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        double value = 1;

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void getDefaultString() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        String value = "1";

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void getDefaultBoolean() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        boolean value = true;

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void getDefaultEnum() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Settings settings = new Settings();
        String key = "test";
        TestValue value = TestValue.ONE;

        assertEquals(value, settings.get(key, value));
    }

    @Test
    void setInt() {
        Settings settings = new Settings();
        String key = "test";
        int value = 1;

        settings.set(key, value);

        assertEquals(value, settings.get(key, 0));
    }

    @Test
    void setLong() {
        Settings settings = new Settings();
        String key = "test";
        long value = 1;

        settings.set(key, value);

        assertEquals(value, settings.get(key, 0L));
    }

    @Test
    void setFloat() {
        Settings settings = new Settings();
        String key = "test";
        float value = 1;

        settings.set(key, value);

        assertEquals(value, settings.get(key, .0f));
    }

    @Test
    void setDouble() {
        Settings settings = new Settings();
        String key = "test";
        double value = 1;

        settings.set(key, value);

        assertEquals(value, settings.get(key, .0));
    }

    @Test
    void setString() {
        Settings settings = new Settings();
        String key = "test";
        String value = "1";

        settings.set(key, value);

        assertEquals(value, settings.get(key, ""));
    }

    @Test
    void setBoolean() {
        Settings settings = new Settings();
        String key = "test";
        boolean value = true;

        settings.set(key, value);

        assertEquals(value, settings.get(key, false));
    }

    @Test
    void setEnum() {
        Settings settings = new Settings();
        String key = "test";
        TestValue value = TestValue.ONE;

        settings.set(key, value);

        assertEquals(value, settings.get(key, TestValue.TWO));
    }
}
