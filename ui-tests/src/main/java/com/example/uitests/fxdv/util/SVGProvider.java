package com.example.uitests.fxdv.util;

import javafx.scene.image.Image;
import org.girod.javafx.svgimage.SVGLoader;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This calls provides SVG images from the resources.
 */
public final class SVGProvider {
    private static final Map<String, Image> cashedIcons = new HashMap<>();

    /**
     * Loads the SVG image with the specified ID from the application's
     * resources.
     * <p>
     * The image has to be located in the "svg" resource folder.
     * folder.
     *
     * @param id The file name of the image without the extension. ".svg" is
     *        automatically appended.
     * @param size The width and the height of the returned Icon;
     *
     * @return The loaded icon.
     *
     * @throws NullPointerException {@code id} is {@code null}.
     * @throws IllegalArgumentException There is no image with the given ID
     *         found.
     */
    public static Image get(String id, int size) {
        Objects.requireNonNull(id, "id must not be null.");

        String chashId = id + size;

        Image image = cashedIcons.get(chashId);

        if (image != null)
            return image;

        URL url = SVGProvider.class.getResource("/com/example/uitests/swing/images/" + id + ".svg");

        if (url == null)
            throw new IllegalArgumentException("Image not found: " + id);

        image = SVGLoader.load(url).toImage(size);

        cashedIcons.put(chashId, image);

        return image;
    }

    private SVGProvider() {
    }
}
