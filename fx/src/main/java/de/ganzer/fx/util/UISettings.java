package de.ganzer.fx.util;

import de.ganzer.core.util.DuplicateSettingException;
import de.ganzer.core.util.UserSettings;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
     * {@inheritDoc}
     */
    public UISettings(String appName, String appVersion, String fileName) {
        super(appName, appVersion, fileName);
    }

    /**
     * Writes the bounds and state of the specified stage.
     *
     * @param key The ID of the settings to write.
     * @param stage The stage to write.
     */
    public void write(String key, Stage stage) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(stage, "stage must not be null");

        boolean maximized = stage.isMaximized();
        set(key + KEY_STATE, maximized);

        if (!maximized) {
            set(key + KEY_X, stage.getX());
            set(key + KEY_WIDTH, stage.getWidth());
            set(key + KEY_Y, stage.getY());
            set(key + KEY_HEIGHT, stage.getHeight());
        }
    }

    /**
     * Applies saved bounds and state to the specified stage.
     * <p>
     * If the top left corner of the stage is not visible in the range of the
     * current desktop, the stage is moved and maybe resized to be fully visible.
     *
     * @param key The ID of the settings to apply.
     * @param stage The stage to adjust.
     */
    public void apply(String key, Stage stage) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(stage, "stage must not be null");

        boolean maximized = get(key + KEY_STATE, false);
        double x = get(key + KEY_X, stage.getX());
        double y = get(key + KEY_Y, stage.getY());
        double width = get(key + KEY_WIDTH, stage.getWidth());
        double height = get(key + KEY_HEIGHT, stage.getHeight());

        Rectangle2D bounds = clamp(getDisplays(), new Rectangle2D(x, y, width, height));

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        stage.setFullScreen(maximized);
    }

    private static Rectangle2D clamp(List<Rectangle2D> displays, Rectangle2D bounds) {
        if (displays.size() == 1)
            return clampToPrime(bounds);

        for (Rectangle2D r: displays) {
            if (r.contains(bounds))
                return bounds;
        }

        List<Rectangle2D> intersections = displays.stream()
                .filter(bounds::intersects)
                .toList();

        if (intersections.size() == 1)
            return clampTo(intersections.get(0), bounds);

        for (Rectangle2D r: intersections) {
            if (r.contains(bounds))
                return bounds;
        }

        return clampToPrime(bounds);
    }

    private static Rectangle2D clampToPrime(Rectangle2D bounds) {
        return clampTo(Screen.getPrimary().getBounds(), bounds);
    }

    private static Rectangle2D clampTo(Rectangle2D screenBounds, Rectangle2D stageBounds) {
        double x = stageBounds.getMinX();
        double y = stageBounds.getMinY();
        double width = stageBounds.getWidth();
        double height = stageBounds.getHeight();

        if (x + width > screenBounds.getWidth())
            x = (int)screenBounds.getWidth() - width;

        if (y + height > screenBounds.getHeight())
            y = (int)screenBounds.getHeight() - height;

        if (x < 0)
            x = 0;

        if (y < 0)
            y = 0;

        return new Rectangle2D(x, y, width, height);
    }

    private static boolean isNotStateSet(int states, int state) {
        return (states & state) != state;
    }

    private static List<Rectangle2D> getDisplays() {
        return Screen.getScreens().stream()
                .map(Screen::getBounds)
                .sorted(Comparator.comparing(Rectangle2D::getMinX))
                .toList();
    }
}
