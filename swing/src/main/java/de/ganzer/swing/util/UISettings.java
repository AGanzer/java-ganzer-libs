package de.ganzer.swing.util;

import de.ganzer.core.util.DuplicateSettingException;
import de.ganzer.core.util.UserSettings;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Extends the {@link UserSettings} to enable Windows to save and restore their
 * settings.
 */
@SuppressWarnings("unused")
public class UISettings extends UserSettings {
    private static final String KEY_STATE = ".state";
    private static final String KEY_X = ".x";
    private static final String KEY_Y = ".y";
    private static final String KEY_WIDTH = ".width";
    private static final String KEY_HEIGHT = ".height";

    /**
     * Creates a new instance with a file name set to "ui".
     *
     * @param appName The name of the application where the name of the settings
     *         file is build from.
     * @param appVersion The version of the application where the name of the
     *         settings sub folder is build from.
     *
     * @throws IllegalArgumentException {@code appName} or {@code appVersion} is
     *         {@code null} or empty or contain only whitespaces.
     * @throws DuplicateSettingException if a setting with the file name "ui"
     *         does already exist for {@code appName} and {@code appVersion}.
     */
    public UISettings(String appName, String appVersion) {
        super(appName, appVersion, "ui");
    }

    /**
     * Creates a new instance with a file name set to "ui".
     *
     * @param appName The name of the application where the name of the settings
     *         file is build from.
     * @param appVersion The version of the application where the name of the
     *         settings sub folder is build from.
     * @param asXml Indicates whether the settings file shall be written in XML.
     *
     * @throws IllegalArgumentException {@code appName} or {@code appVersion} is
     *         {@code null} or empty or contain only whitespaces.
     * @throws DuplicateSettingException if a setting with the file name "ui"
     *         does already exist for {@code appName} and {@code appVersion}.
     */
    public UISettings(String appName, String appVersion, boolean asXml) {
        super(appName, appVersion, "ui", asXml);
    }

    /**
     * {@inheritDoc}
     */
    public UISettings(String appName, String appVersion, String fileName) {
        super(appName, appVersion, fileName);
    }

    /**
     * {@inheritDoc}
     */
    public UISettings(String appName, String appVersion, String fileName, boolean asXml) {
        super(appName, appVersion, fileName, asXml);
    }

    /**
     * Writes the bounds and state of the specified frame.
     *
     * @param key The ID of the settings to write.
     * @param frame The frame to write.
     */
    public void write(String key, JFrame frame) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(frame, "frame must not be null");

        int state = frame.getExtendedState();
        write(key + KEY_STATE, state);

        writeWindowSettings(key, frame, state);
    }

    /**
     * Writes the bounds of the specified dialog.
     *
     * @param key The ID of the settings to write.
     * @param dialog The dialog to write.
     */
    public void write(String key, JDialog dialog) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(dialog, "dialog must not be null");

        writeWindowSettings(key, dialog, 0);
    }

    /**
     * Applies saved bounds and state to the specified frame.
     * <p>
     * If the top left corner of the frame is not visible in the range of the
     * current desktop, the frame is moved and maybe resized to be fully visible.
     *
     * @param key The ID of the settings to apply.
     * @param frame The frame to adjust.
     */
    public void apply(String key, JFrame frame) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(frame, "frame must not be null");

        applyWindowSettings(key, frame);

        int state = read(key + KEY_STATE, JFrame.NORMAL);
        frame.setExtendedState(state &~ JFrame.ICONIFIED);
    }

    /**
     * Applies saved bounds to the specified dialog.
     * <p>
     * If the top left corner of the dialog is not visible in the range of the
     * current desktop, the dialog is moved and maybe resized to be fully visible.
     *
     * @param key The ID of the settings to apply.
     * @param dialog The dialog to adjust.
     */
    public void apply(String key, JDialog dialog) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(dialog, "dialog must not be null");

        applyWindowSettings(key, dialog);
    }

    private void writeWindowSettings(String key, Window window, int state) {
        if (isNotStateSet(state, JFrame.MAXIMIZED_HORIZ)) {
            write(key + KEY_X, window.getX());
            write(key + KEY_WIDTH, window.getWidth());
        }

        if (isNotStateSet(state, JFrame.MAXIMIZED_VERT)) {
            write(key + KEY_Y, window.getY());
            write(key + KEY_HEIGHT, window.getHeight());
        }
    }

    private void applyWindowSettings(String key, Window window) {
        int x = read(key + KEY_X, window.getX());
        int y = read(key + KEY_Y, window.getY());
        int width = read(key + KEY_WIDTH, window.getWidth());
        int height = read(key + KEY_HEIGHT, window.getHeight());

        window.setBounds(x, y, width, height);

        clamp(getDisplays(), window);
    }

    private static void clamp(List<Rectangle> displays, Window window) {
        if (displays.size() == 1)
            clampToPrime(window);
        else {
            Rectangle bounds = window.getBounds();

            for (Rectangle r: displays) {
                if (r.contains(bounds))
                    return;
            }

            List<Rectangle> intersections = displays.stream()
                    .filter(r -> !bounds.intersection(r).isEmpty())
                    .toList();

            if (intersections.size() == 1) {
                clampTo(intersections.get(0), window);
                return;
            }

            for (Rectangle r: intersections) {
                if (r.contains(bounds.getLocation()))
                    return;
            }

            clampToPrime(window);
        }
    }

    private static void clampToPrime(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int desktopWidth = (int)screenSize.getWidth();
        int desktopHeight = (int)screenSize.getHeight();
        Rectangle bounds = new Rectangle(0, 0, desktopWidth, desktopHeight);

        clampTo(bounds, window);
    }

    private static void clampTo(Rectangle display, Window window) {
        int x = window.getX();
        int y = window.getY();
        int width = window.getWidth();
        int height = window.getHeight();

        if (x + width > display.x + display.width)
            x -= (x + width) - (display.x + display.width);

        if (y + height > display.y + display.height)
            y -= (y + height) - (display.y + display.height);

        if (x < 0)
            x = 0;

        if (y < 0)
            y = 0;

        window.setBounds(x, y, width, height);
    }

    private static boolean isNotStateSet(int states, int state) {
        return (states & state) != state;
    }

    private static List<Rectangle> getDisplays() {
        return Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
                .map(GraphicsDevice::getDefaultConfiguration)
                .map(config -> {
                    DisplayMode dm = config.getDevice().getDisplayMode();
                    Rectangle bounds = config.getBounds();

                    return new Rectangle((int)bounds.getX(), (int)bounds.getY(), dm.getWidth(), dm.getHeight());
                })
                .sorted(Comparator.comparing(Rectangle::getX))
                .toList();
    }
}
