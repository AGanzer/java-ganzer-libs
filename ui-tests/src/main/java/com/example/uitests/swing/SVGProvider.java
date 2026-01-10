package com.example.uitests.swing;

import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.app.beans.SVGIcon;
import de.ganzer.core.util.Strings;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This calls provides SVG images from the resources.
 */
public final class SVGProvider {
    private static final Map<String, SVGIcon> cashedIcons = new HashMap<>();

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
    public static ImageIcon get(String id, int size) {
        return get(id, size, size);
    }

    /**
     * Loads the SVG image with the specified ID from the application's
     * resources.
     * <p>
     * The image has to be located in the "svg" resource folder.
     * folder.
     *
     * @param id The file name of the image without the extension. ".svg" is
     *        automatically appended.
     * @param width The width of the returned Icon;
     * @param height The height of the returned Icon;
     *
     * @return The loaded icon.
     *
     * @throws NullPointerException {@code id} is {@code null}.
     * @throws IllegalArgumentException There is no image with the given ID
     *         found.
     */
    public static ImageIcon get(String id, int width, int height) {
        Objects.requireNonNull(id, "id must not be null.");

        String chashId = id + width + height;

        SVGIcon icon = cashedIcons.get(chashId);

        if (icon != null)
            return icon;

        try {
            URL url = SVGProvider.class.getResource("/com/example/uitests/swing/images/" + id + ".svg");

            if (url == null)
                throw new IllegalArgumentException("Image not found: " + id);

            icon = new SVGIcon();
            icon.setSvgURI(url.toURI());
            icon.setPreferredSize(new Dimension(width, height));
            icon.setAutosize(SVGIcon.AUTOSIZE_STRETCH);
            icon.setAntiAlias(true);

            cashedIcons.put(chashId, icon);

            return icon;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Image not found: " + id);
        }
    }

    /**
     * Creates an icon that contains the specified text.
     *
     * @param text The text to set as image.
     * @param width The width of the icon.
     * @param height The height of the icon.
     * @param pos The top-left position of the text within the icon.
     * @param bg The background color to set.
     * @param fg The foreground color to set.
     * @param f The font to use.
     *
     * @return The created icon or {@code null} if {@code text} is empty or
     *         {@code null} or if an error occurred.
     */
    public static ImageIcon getFromText(String text, int width, int height, Point pos, Color bg, Color fg, Font f) {
        if (Strings.isNullOrEmpty(text))
            return null;

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);

        SVGGraphics2D g2d = new SVGGraphics2D(document);
        g2d.setSVGCanvasSize(new Dimension(width, height));

        g2d.setColor(bg);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(fg);
        g2d.setFont(f);
        g2d.drawString(text, pos.x, pos.y);

        StringWriter writer = new StringWriter();

        try {
            g2d.stream(writer, true);
        } catch (SVGGraphics2DIOException e) {
            e.printStackTrace(System.err);
            return null;
        }

        SVGUniverse universe = new SVGUniverse();
        SVGIcon icon = new SVGIcon();
        icon.setSvgUniverse(universe);
        icon.setSvgURI(universe.loadSVG(new StringReader(writer.toString()), "string.svg"));
        icon.setPreferredSize(new Dimension(width, height));
        icon.setAutosize(SVGIcon.AUTOSIZE_STRETCH);
        icon.setAntiAlias(true);

        return icon;
    }
}
