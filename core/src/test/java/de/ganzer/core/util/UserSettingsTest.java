package de.ganzer.core.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserSettingsTest {
    @Test
    void createUserSettings() throws IOException {
        String appName = "test";
        String appVersion = "1.0.0";
        String fileName = "properties";

        //noinspection MismatchedQueryAndUpdateOfCollection
        UserSettings settings = new UserSettings(appName, appVersion, fileName);

        assertEquals(appName, settings.getAppName());
        assertEquals(appVersion, settings.getAppVersion());
        assertEquals(fileName, settings.getFileName());
    }

    @Test
    void createUserSettingsDefaultFileName() throws IOException {
        String appName = "test";
        String appVersion = "1.0.0";
        String fileName = "settings";

        //noinspection MismatchedQueryAndUpdateOfCollection
        UserSettings settings = new UserSettings(appName, appVersion);

        assertEquals(appName, settings.getAppName());
        assertEquals(appVersion, settings.getAppVersion());
        assertEquals(fileName, settings.getFileName());
    }

    @Test
    void createUserSettingsInvalidFileName() {
        String appName = "test";
        String appVersion = "1.0.0";
        String fileName = "a/b";

        assertThrows(IllegalArgumentException.class, () -> new UserSettings(appName, appVersion, fileName));
    }

    @Test
    void createUserSettingsInvalidFileName2() {
        String appName = "test";
        String appVersion = "1.0.0";
        String fileName = "";

        assertThrows(IllegalArgumentException.class, () -> new UserSettings(appName, appVersion, fileName));
    }

    @Test
    void createUserSettingsInvalidAppName() {
        String appName = "";
        String appVersion = "1.0.0";

        assertThrows(IllegalArgumentException.class, () -> new UserSettings(appName, appVersion));
    }

    @Test
    void createUserSettingsInvalidAppName2() {
        String appName = "   ";
        String appVersion = "1.0.0";

        assertThrows(IllegalArgumentException.class, () -> new UserSettings(appName, appVersion));
    }

    @Test
    void createUserSettingsInvalidVersion() {
        String appName = "test";
        String appVersion = "";

        assertThrows(IllegalArgumentException.class, () -> new UserSettings(appName, appVersion));
    }

    @Test
    void createUserSettingsInvalidVersion2() {
        String appName = "test";
        String appVersion = "   ";

        assertThrows(IllegalArgumentException.class, () -> new UserSettings(appName, appVersion));
    }

    @Test
    void createUserSettingsDuplicateEntry() throws IOException {
        String appName = "test";
        String appVersion = "1.0.0";
        String fileName = "testsettings";

        new UserSettings(appName, appVersion, fileName);
        
        assertThrows(DuplicateSettingException.class, () -> new UserSettings(appName, appVersion, fileName));
    }
}