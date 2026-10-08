package com.example.uitests.swingdv.doc.ol;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A Swing component for displaying a turtle drawing.
 *
 * <p>The turtle commands are interpreted as follows:</p>
 *
 * <ul>
 *   <li>{@code F} - Move forward and draw a line.</li>
 *   <li>{@code f} - Move forward without drawing.</li>
 *   <li>{@code +} - Turn clockwise by the configured angle.</li>
 *   <li>{@code -} - Turn counterclockwise by the configured angle.</li>
 *   <li>All other characters are ignored.</li>
 * </ul>
 *
 * <p>The turtle uses a mathematical coordinate system internally:</p>
 *
 * <ul>
 *   <li>X increases to the right.</li>
 *   <li>Y increases upwards.</li>
 * </ul>
 *
 * <p>When the component is painted, the coordinate system is automatically
 * scaled and centered so that the complete drawing fits into the available
 * area of the panel while preserving its aspect ratio.</p>
 *
 * <p>The line width is not scaled. It is specified in screen pixels and
 * therefore remains constant regardless of the scale factor used to fit
 * the drawing into the component.</p>
 */
public class OLCanvas extends JPanel {
    private String movements = "";
    private double stepLength = 10.0;
    private double turnAngle = 90.0;
    private Color lineColor = Color.BLACK;
    private float lineWidth = 1.0f;
    private int padding = 10;

    /**
     * Creates a new {@code OLCanvas}.
     */
    public OLCanvas() {
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    /**
     * Sets the turtle command sequence.
     *
     * @param movements the command sequence, for example {@code "F+F+F+F"}
     */
    public void setMovements(String movements) {
        this.movements = movements != null ? movements : "";
        repaint();
    }

    /**
     * Returns the current turtle command sequence.
     *
     * @return the turtle command sequence
     */
    public String getMovements() {
        return movements;
    }

    /**
     * Sets the length of one turtle step.
     *
     * @param stepLength the step length in logical units; must be greater
     *                   than zero
     *
     * @throws IllegalArgumentException if {@code stepLength} is less than
     *                                  or equal to zero
     */
    public void setStepLength(double stepLength) {
        if (stepLength <= 0) {
            throw new IllegalArgumentException(
                    "The step length must be greater than zero."
            );
        }

        this.stepLength = stepLength;
        repaint();
    }

    /**
     * Returns the length of one turtle step.
     *
     * @return the step length in logical units
     */
    public double getStepLength() {
        return stepLength;
    }

    /**
     * Sets the turtle's turn angle.
     *
     * @param turnAngle the turn angle in degrees
     */
    public void setTurnAngle(double turnAngle) {
        this.turnAngle = turnAngle;
        repaint();
    }

    /**
     * Returns the turtle's turn angle.
     *
     * @return the turn angle in degrees
     */
    public double getTurnAngle() {
        return turnAngle;
    }

    /**
     * Sets the color used for drawing turtle lines.
     *
     * @param lineColor the line color; must not be {@code null}
     *
     * @throws IllegalArgumentException if {@code lineColor} is {@code null}
     */
    public void setLineColor(Color lineColor) {
        if (lineColor == null) {
            throw new IllegalArgumentException(
                    "The line color must not be null."
            );
        }

        this.lineColor = lineColor;
        repaint();
    }

    /**
     * Returns the current line color.
     *
     * @return the line color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Sets the line width.
     *
     * <p>The width is specified in screen pixels and is not affected by
     * the scale factor used to fit the drawing into the component.</p>
     *
     * @param lineWidth the line width in screen pixels; must be greater
     *                  than zero
     *
     * @throws IllegalArgumentException if {@code lineWidth} is less than
     *                                  or equal to zero
     */
    public void setLineWidth(float lineWidth) {
        if (lineWidth <= 0) {
            throw new IllegalArgumentException(
                    "The line width must be greater than zero."
            );
        }

        this.lineWidth = lineWidth;
        repaint();
    }

    /**
     * Returns the current line width.
     *
     * @return the line width in screen pixels
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Sets the minimum distance between the drawing and the panel border.
     *
     * @param padding the padding in screen pixels; must not be negative
     *
     * @throws IllegalArgumentException if {@code padding} is negative
     */
    public void setPadding(int padding) {
        if (padding < 0) {
            throw new IllegalArgumentException(
                    "The padding must not be negative."
            );
        }

        this.padding = padding;
        repaint();
    }

    /**
     * Returns the current padding.
     *
     * @return the padding in screen pixels
     */
    public int getPadding() {
        return padding;
    }

    /**
     * Converts the turtle command sequence into the actual line segments
     * produced by the turtle.
     *
     * <p>The turtle starts at coordinate {@code (0, 0)} with a heading of
     * zero degrees. A heading of zero degrees points to the right.</p>
     *
     * @return a list containing all line segments drawn by the turtle
     */
    private List<Line2D.Double> createLines() {
        List<Line2D.Double> lines = new ArrayList<>();

        // Initial position.
        double x = 0.0;
        double y = 0.0;

        // Zero degrees means pointing to the right.
        double angle = 0.0;

        for (int i = 0; i < movements.length(); i++) {

            char command = movements.charAt(i);

            switch (command) {

                case 'F': {
                    double radians = Math.toRadians(angle);

                    double newX =
                            x + Math.cos(radians) * stepLength;

                    double newY =
                            y + Math.sin(radians) * stepLength;

                    lines.add(new Line2D.Double(
                            x, y,
                            newX, newY
                    ));

                    x = newX;
                    y = newY;

                    break;
                }

                case 'f': {
                    double radians = Math.toRadians(angle);

                    x += Math.cos(radians) * stepLength;
                    y += Math.sin(radians) * stepLength;

                    break;
                }

                case '+':
                    angle += turnAngle;
                    break;

                case '-':
                    angle -= turnAngle;
                    break;

                default:
                    // Ignore all other characters.
                    break;
            }
        }

        return lines;
    }

    /**
     * Calculates the bounding box containing all generated line segments.
     *
     * @param lines the line segments to evaluate
     * @return the bounding box, or {@code null} if the list is empty
     */
    private Rectangle2D.Double calculateBounds(
            List<Line2D.Double> lines) {

        if (lines.isEmpty()) {
            return null;
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Line2D.Double line : lines) {

            minX = Math.min(minX,
                            Math.min(line.x1, line.x2));

            minY = Math.min(minY,
                            Math.min(line.y1, line.y2));

            maxX = Math.max(maxX,
                            Math.max(line.x1, line.x2));

            maxY = Math.max(maxY,
                            Math.max(line.y1, line.y2));
        }

        return new Rectangle2D.Double(
                minX,
                minY,
                maxX - minX,
                maxY - minY
        );
    }

    /**
     * Paints the turtle drawing.
     *
     * <p>The drawing is scaled uniformly to fit into the available panel
     * area. Its aspect ratio is preserved. The drawing is also centered
     * within the panel.</p>
     *
     * <p>The geometric transformation is applied to the line shapes rather
     * than to the graphics context used for stroking them. This ensures
     * that the configured line width remains constant in screen pixels
     * even when the drawing itself is scaled.</p>
     *
     * @param g the {@link Graphics} context used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        List<Line2D.Double> lines = createLines();

        if (lines.isEmpty()) {
            return;
        }

        Rectangle2D.Double bounds = calculateBounds(lines);

        if (bounds == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        try {

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            /*
             * Calculate the available drawing area.
             */
            double availableWidth =
                    getWidth() - 2.0 * padding;

            double availableHeight =
                    getHeight() - 2.0 * padding;

            if (availableWidth <= 0 || availableHeight <= 0) {
                return;
            }

            /*
             * Handle the degenerate case where the drawing has no extent.
             */
            if (bounds.width <= 0 && bounds.height <= 0) {
                return;
            }

            /*
             * Calculate a uniform scale factor.
             *
             * Using Math.min() preserves the aspect ratio of the drawing.
             */
            double scaleX =
                    availableWidth / Math.max(bounds.width, 1e-12);

            double scaleY =
                    availableHeight / Math.max(bounds.height, 1e-12);

            double scale = Math.min(scaleX, scaleY);

            /*
             * Calculate the center of the drawing in logical coordinates.
             */
            double centerX =
                    bounds.x + bounds.width / 2.0;

            double centerY =
                    bounds.y + bounds.height / 2.0;

            /*
             * Calculate the center of the panel.
             */
            double panelCenterX =
                    getWidth() / 2.0;

            double panelCenterY =
                    getHeight() / 2.0;

            /*
             * Create the transformation:
             *
             * 1. Move the drawing center to the origin.
             * 2. Scale the drawing uniformly.
             * 3. Flip the Y axis so that the mathematical coordinate
             *    system points upwards.
             * 4. Move the drawing to the center of the panel.
             */
            AffineTransform transform =
                    new AffineTransform();

            transform.translate(
                    panelCenterX,
                    panelCenterY
            );

            transform.scale(
                    scale,
                    -scale
            );

            transform.translate(
                    -centerX,
                    -centerY
            );

            /*
             * Use a fixed-width stroke. The stroke width is therefore
             * independent of the geometric scale factor.
             */
            g2.setColor(lineColor);

            BasicStroke stroke =
                    new BasicStroke(
                            lineWidth,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    );

            g2.setStroke(stroke);

            /*
             * Transform and draw every line segment.
             */
            for (Line2D.Double line : lines) {

                Shape transformedLine =
                        transform.createTransformedShape(line);

                g2.draw(transformedLine);
            }

        } finally {
            g2.dispose();
        }
    }
}
