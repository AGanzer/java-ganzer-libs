package com.example.uitests.fxdv.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.*;

/**
 * This calls provides PNG images from the resources.
 */
public final class ImageProvider {
    private static final Map<String, Image> cashedIcons = new HashMap<>();

    /**
     * Gets the images to use as window and application icons.
     *
     * @return A collection that contains all required images.
     */
    public static List<Image> getApplicationIcons() {
        List<Image> images = new ArrayList<>();

        images.add(get("multimeter_analog_16"));
        images.add(get("multimeter_analog_24"));
        images.add(get("multimeter_analog_32"));
        images.add(get("multimeter_analog_48"));
        images.add(get("multimeter_analog_64"));
        images.add(get("multimeter_analog_128"));
        images.add(get("multimeter_analog_256"));

        return images;
    }

    /**
     * Loads the PNG image with the specified ID from the application's
     * resources.
     * <p>
     * The image has to be located in the "/de/ganzer/devtools/cb/images"
     * resource folder.
     *
     * @param id The file name of the image without the extension. ".png" is
     *        automatically appended.
     *
     * @return The loaded icon.
     *
     * @throws NullPointerException {@code id} is {@code null}.
     * @throws IllegalArgumentException There is no image with the given ID
     *         found.
     */
    public static Image get(String id) {
        Objects.requireNonNull(id, "id must not be null.");

        Image icon = cashedIcons.get(id);

        if (icon != null)
            return icon;

        try (var stream = ImageProvider.class.getResourceAsStream("/com/example/uitests/fxdv/images/" + id + ".png")) {
            if (stream == null)
                throw new IllegalArgumentException("Image not found: " + id);

            icon = new Image(stream);
            cashedIcons.put(id, icon);

            return icon;
        } catch (IOException e) {
            throw new IllegalArgumentException("Image cannot be read: " + id);
        }
    }
}
