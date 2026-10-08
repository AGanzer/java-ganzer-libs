package com.example.uitests.swingdv;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Utility class for creating thumbnails from Swing components.
 * <p>
 * The component is scaled proportionally so that it fits completely
 * within the requested thumbnail dimensions while preserving its original
 * aspect ratio.
 * <p>
 * The resulting {@link BufferedImage} always has exactly the requested
 * width and height. If the aspect ratio of the component differs from the
 * aspect ratio of the thumbnail, the unused area remains transparent.
 */
public final class Thumbnail {
    /**
     * Creates a thumbnail from the specified Swing component.
     * <p>
     * The component is scaled proportionally to fit within the requested
     * thumbnail dimensions. The entire component remains visible; it is
     * never cropped or distorted.
     * <p>
     * The component is centered within the resulting image. If the
     * aspect ratio of the component does not match the requested thumbnail
     * aspect ratio, the remaining area is transparent.
     * <p>
     * For example, a component with a size of {@code 800 x 600} rendered
     * into a thumbnail of {@code 200 x 200} will be scaled to
     * {@code 200 x 150} and centered vertically.
     *
     * @param component The component to render.
     * @param thumbnailWidth The requested thumbnail width in pixels.
     * @param thumbnailHeight The requested thumbnail height in pixels.
     *
     * @return A {@link BufferedImage} with the requested dimensions or
     *         {@code null} if the component's size is invalid or cannot be
     *         calculated.
     *
     * @throws NullPointerException if {@code component} is {@code null}.
     * @throws IllegalArgumentException if either thumbnail dimension is less
     *         than or equal to zero, or if the component does not have a valid
     *         size
     */
    public static BufferedImage create(JComponent component, int thumbnailWidth, int thumbnailHeight) {
        Objects.requireNonNull(component, "component must not be null.");

        if (thumbnailWidth <= 0 || thumbnailHeight <= 0)
            throw new IllegalArgumentException("Thumbnail width and height must be greater than zero.");

        Dimension size = component.getSize();

        if (size.width <= 0 || size.height <= 0)
            size = component.getPreferredSize();

        if (size.width <= 0 || size.height <= 0)
            return null;

        double scaleX = (double) thumbnailWidth / size.width;
        double scaleY = (double) thumbnailHeight / size.height;
        double scale = Math.min(scaleX, scaleY);

        int scaledWidth = Math.max(1, (int) Math.round(size.width * scale));
        int scaledHeight = Math.max(1, (int) Math.round(size.height * scale));

        BufferedImage image = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = (thumbnailWidth - scaledWidth) / 2;
            int y = (thumbnailHeight - scaledHeight) / 2;

            graphics.translate(x, y);
            graphics.scale(scale, scale);

            component.printAll(graphics);
        } finally {
            graphics.dispose();
        }

        return image;
    }

    private Thumbnail() {
    }
}
