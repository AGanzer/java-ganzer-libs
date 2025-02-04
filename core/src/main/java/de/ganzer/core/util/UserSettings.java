package de.ganzer.core.util;

import de.ganzer.core.internals.CoreMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.WeakHashMap;

/**
 * Utility for reading and writing user specific settings.
 * <p>
 * This implementation extends the {@link Settings} class with the ability to
 * read settings from and write settings to a file within the system defined
 * configuration folder of the current user.
 * <p>
 * On Windows, the configuration is file is located in "$HOME\AppDate\Roaming";
 * on all other systems in "$HOME/.config".
 * <p>
 * To prevent multiple settings from using the same file and overwriting each
 * other, a {@link DuplicateSettingException} is thrown if two settings with
 * the same file name are created for the same application and version.
 */
public class UserSettings extends Settings {
    private static final WeakHashMap<String, UserSettings> settings = new WeakHashMap<>();

    private final String appName;
    private final String appVersion;
    private final String fileName;

    /**
     * Creates a new instance with a file name set to "settings".
     * <p>
     * After creation {@link #load()} should be called to read the settings from
     * file.
     *
     * @param appName The name of the application where the name of the settings
     *         file is build from.
     * @param appVersion The version of the application where the name of the
     *         settings sub folder is build from.
     *
     * @throws IllegalArgumentException {@code appName} or {@code appVersion} is
     *         {@code null} or empty or contain only whitespaces.
     * @throws DuplicateSettingException if a setting with the file name "settings"
     *         does already exist for {@code appName} and {@code appVersion}.
     */
    public UserSettings(String appName, String appVersion) {
        this(appName, appVersion, null);
    }

    /**
     * Creates a new instance and loads the settings from the settings file.
     * <p>
     * After creation {@link #load()} should be called to read the settings from
     * file.
     *
     * @param appName The name of the application where the name of the settings
     *         file is build from.
     * @param appVersion The version of the application where the name of the
     *         settings sub folder is build from.
     * @param fileName The name of the settings file. If this is {@code null},
     *         "settings" is used. This must not contain any path information.
     *
     * @throws IllegalArgumentException {@code appName} or {@code appVersion} is
     *         {@code null} or empty or contain only whitespaces or {@code fileName}
     *         is empty or contains whitespaces only or is not a valid name for
     *         files.
     * @throws DuplicateSettingException if a setting with the specified file name
     *         does already exist for {@code appName} and {@code appVersion}.
     */
    public UserSettings(String appName, String appVersion, String fileName) {
        if (Strings.isNullOrBlank(appName))
            throw new IllegalArgumentException("appName must not be null or empty.");

        if (Strings.isNullOrBlank(appVersion))
            throw new IllegalArgumentException("appVersion must not be null or empty.");

        if (fileName != null) {
            if (Strings.isNullOrBlank(fileName) || !FileNames.isValidName(fileName))
                throw new IllegalArgumentException("fileName is not a valid file name.");
        }

        this.appName = appName;
        this.appVersion = appVersion;
        this.fileName = fileName == null ? "settings" : fileName;

        var key = this.appName + this.appVersion + this.fileName;

        if (settings.get(key) != null)
            throw new DuplicateSettingException(this.fileName);

        settings.put(key, this);
    }

    /**
     * Gets the name of the application where the name of the settings file is
     * build from.
     *
     * @return The name of the application that was set at construction.
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Gets the version of the application where the name of the settings sub
     * folder is build from.
     *
     * @return The version of the application that was set at construction.
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Gets the name of the file where to the settings shall be read from and
     * written into.
     *
     * @return The name of the file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Writes the properties into the file within the user's configuration folder.
     *
     * @throws IOException On any I/O error.
     */
    public void save() throws IOException {
        try (FileOutputStream out = new FileOutputStream(getSettingsStoragePath())) {
            store(out, String.format("Settings of %s %s", appName, appVersion));
        } catch (Exception e) {
            throw new IOException(CoreMessages.get("error.cannotStoreSettings", getSettingsStoragePath()), e);
        }
    }

    /**
     * Loads the properties from the file within the user's configuration folder.
     * <p>
     * This is automatically called at construction and can be used to reload the
     * settings.
     *
     * @throws IOException On any I/O error. This does not occur when the settings
     *         file does not exist.
     */
    public void load() throws IOException {
        try {
            if (!new File(getSettingsStoragePath()).exists())
                return;

            try (FileInputStream in = new FileInputStream(getSettingsStoragePath())) {
                load(in);
            }
        } catch (Exception e) {
            throw new IOException(CoreMessages.get("error.cannotLoadSettings", getSettingsStoragePath()), e);
        }
    }

    private String getSettingsStoragePath() {
        String home = System.getProperty("user.home");
        String osName = System.getProperty("os.name");
        String path;

        if (osName.startsWith("Windows"))
            path = Path.of(home, "AppData", "Roaming", FileNames.getValidName(appName), FileNames.getValidName(appVersion)).toString();
        else
            path = Path.of(home, ".config", FileNames.getValidName(appName), FileNames.getValidName(appVersion)).toString();

        File file = new File(path);
        file.mkdirs();

        return Path.of(path, fileName).toString();
    }
}
