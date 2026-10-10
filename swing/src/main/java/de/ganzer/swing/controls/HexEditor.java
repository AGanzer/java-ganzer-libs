package de.ganzer.swing.controls;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A split-pane component that provides a synchronized hexadecimal and textual
 * editor for character data.
 * <p>
 * The left view displays characters as four-digit hexadecimal Unicode values
 * separated by spaces. The right view displays the textual representation of
 * the characters.
 *
 * @since 6.0.0
 */
@SuppressWarnings("unused")
public class HexEditor extends JSplitPane {
    private final StringBuilder content = new StringBuilder();
    private final HexView hexView;
    private final TextView textView;
    private final JScrollPane hexScrollPane;
    private final JScrollPane textScrollPane;

    private int caretPosition = 0;
    private int hexCaretPosition = 0;
    private boolean editable = true;
    private Color caretForeground;
    private Color caretBackground;
    private boolean customCaretForeground = false;
    private boolean customCaretBackground = false;

    /**
     * Creates a new empty {@link HexEditor} with default settings.
     */
    public HexEditor() {
        this("");
    }

    /**
     * Creates a new {@link HexEditor} initialized with the specified text.
     *
     * @param text The initial text to display, or {@code null} for empty.
     */
    public HexEditor(String text) {
        super(JSplitPane.HORIZONTAL_SPLIT);

        if (text != null) {
            content.append(text);
        }

        Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        super.setFont(defaultFont);

        Color defaultFg = UIManager.getColor("TextArea.foreground");
        if (defaultFg == null) {
            defaultFg = Color.BLACK;
        }
        super.setForeground(defaultFg);

        Color defaultBg = UIManager.getColor("TextArea.background");
        if (defaultBg == null) {
            defaultBg = Color.WHITE;
        }
        super.setBackground(defaultBg);

        caretForeground = invertColor(defaultFg);
        caretBackground = invertColor(defaultBg);

        hexView = new HexView();
        textView = new TextView();

        hexView.setFont(defaultFont);
        hexView.setForeground(defaultFg);
        hexView.setBackground(defaultBg);

        textView.setFont(defaultFont);
        textView.setForeground(defaultFg);
        textView.setBackground(defaultBg);

        hexScrollPane = new JScrollPane(hexView);
        hexScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        hexScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        textScrollPane = new JScrollPane(textView);
        textScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Synchronize vertical scrolling between both scroll panes.
        textScrollPane.getVerticalScrollBar().setModel(
                hexScrollPane.getVerticalScrollBar().getModel());

        setLeftComponent(hexScrollPane);
        setRightComponent(textScrollPane);
        setResizeWeight(0.5);
        setContinuousLayout(true);

        hexView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateViewsLayout();
            }
        });
    }

    /**
     * Inverts the RGB components of a given {@link Color}.
     *
     * @param color The color to invert.
     * @return The inverted color, or {@link Color#BLACK} if {@code null}.
     */
    private static Color invertColor(Color color) {
        if (color == null) {
            return Color.BLACK;
        }
        return new Color(255 - color.getRed(), 255 - color.getGreen(),
                255 - color.getBlue());
    }

    /**
     * Returns the text currently contained in this editor.
     *
     * @return The content text.
     */
    public String getText() {
        return content.toString();
    }

    /**
     * Sets the text displayed and edited by this editor.
     *
     * @param text The new text content, or {@code null} to clear.
     */
    public void setText(String text) {
        content.setLength(0);
        if (text != null) {
            content.append(text);
        }
        setCaretPosition(0);
        updateViewsLayout();
        repaintViews();
    }

    /**
     * Returns the character index of the current caret position.
     *
     * @return The zero-based character index.
     */
    public int getCaretPosition() {
        return caretPosition;
    }

    /**
     * Sets the character index of the caret position.
     *
     * @param position The zero-based character index.
     */
    public void setCaretPosition(int position) {
        int maxPos = content.length();
        this.caretPosition = Math.max(0, Math.min(maxPos, position));
        this.hexCaretPosition = this.caretPosition * 5;
        scrollToCaret();
        repaintViews();
    }

    /**
     * Sets the sub-position of the hex caret.
     *
     * @param hexPosition The zero-based hex caret position.
     */
    void setHexCaretPosition(int hexPosition) {
        int maxPos = content.length() * 5;
        this.hexCaretPosition = Math.max(0, Math.min(maxPos, hexPosition));
        this.caretPosition = Math.min(content.length(), hexCaretPosition / 5);
        scrollToCaret();
        repaintViews();
    }

    /**
     * Returns the sub-position of the hex caret.
     *
     * @return The zero-based hex caret position.
     */
    int getHexCaretPosition() {
        return hexCaretPosition;
    }

    /**
     * Returns whether this editor is editable.
     *
     * @return {@code true} if editable, {@code false} otherwise.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets whether this editor is editable.
     *
     * @param editable {@code true} to allow editing, {@code false} otherwise.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Returns the foreground color used for highlighting the character under
     * the caret.
     *
     * @return The caret highlight foreground color.
     */
    public Color getCaretForeground() {
        return caretForeground;
    }

    /**
     * Sets the foreground color used for highlighting the character under
     * the caret.
     *
     * @param caretForeground The new caret highlight foreground color.
     */
    public void setCaretForeground(Color caretForeground) {
        this.caretForeground = caretForeground;
        this.customCaretForeground = (caretForeground != null);
        repaintViews();
    }

    /**
     * Returns the background color used for highlighting the character under
     * the caret.
     *
     * @return The caret highlight background color.
     */
    public Color getCaretBackground() {
        return caretBackground;
    }

    /**
     * Sets the background color used for highlighting the character under
     * the caret.
     *
     * @param caretBackground The new caret highlight background color.
     */
    public void setCaretBackground(Color caretBackground) {
        this.caretBackground = caretBackground;
        this.customCaretBackground = (caretBackground != null);
        repaintViews();
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (!customCaretForeground) {
            caretForeground = invertColor(fg);
        }
        if (hexView != null) {
            hexView.setForeground(fg);
        }
        if (textView != null) {
            textView.setForeground(fg);
        }
        repaintViews();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (!customCaretBackground) {
            caretBackground = invertColor(bg);
        }
        if (hexView != null) {
            hexView.setBackground(bg);
        }
        if (textView != null) {
            textView.setBackground(bg);
        }
        repaintViews();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (hexView != null) {
            hexView.setFont(font);
        }
        if (textView != null) {
            textView.setFont(font);
        }
        updateViewsLayout();
        repaintViews();
    }

    /**
     * Returns the component displaying the hexadecimal representation.
     *
     * @return The hex view component.
     */
    public JComponent getHexView() {
        return hexView;
    }

    /**
     * Returns the component displaying the textual representation.
     *
     * @return The text view component.
     */
    public JComponent getTextView() {
        return textView;
    }

    /**
     * Returns the scroll pane enclosing the hexadecimal view.
     *
     * @return The hex scroll pane.
     */
    public JScrollPane getHexScrollPane() {
        return hexScrollPane;
    }

    /**
     * Returns the scroll pane enclosing the text view.
     *
     * @return The text scroll pane.
     */
    public JScrollPane getTextScrollPane() {
        return textScrollPane;
    }

    /**
     * Calculates the number of characters displayed per line based on the
     * width of the hex view.
     *
     * @return The number of characters per line, at least 1.
     */
    int getCharsPerLine() {
        Font font = getFont();
        if (font == null || hexView == null) {
            return 16;
        }
        FontMetrics fm = hexView.getFontMetrics(font);
        int charWidth = fm.charWidth('0');
        if (charWidth <= 0) {
            return 16;
        }
        int visibleWidth = hexScrollPane != null
                && hexScrollPane.getViewport() != null
                && hexScrollPane.getViewport().getWidth() > 0
                ? hexScrollPane.getViewport().getWidth() : hexView.getWidth();
        if (visibleWidth <= 0) {
            return 16;
        }
        Insets insets = hexView.getInsets();
        int usableWidth = Math.max(0,
                visibleWidth - insets.left - insets.right);
        int totalCharsFit = usableWidth / charWidth;
        // Each hex word needs 4 chars, plus 1 space separator.
        // N words require (5 * N - 1) characters width.
        return Math.max(1, (totalCharsFit + 1) / 5);
    }

    /**
     * Converts a character into its four-digit uppercase hexadecimal string.
     *
     * @param c The character to convert.
     * @return The four-digit hexadecimal string representation.
     */
    static String getHexCode(char c) {
        return String.format("%04X", (int) c);
    }

    /**
     * Returns the display character for the text view.
     * Non-printable characters, spaces, and control characters are replaced
     * by a dot ('.').
     *
     * @param c The character to convert.
     * @return The displayable character.
     */
    static char getDisplayChar(char c) {
        if (c == ' ' || Character.isWhitespace(c) || Character.isISOControl(c)
                || !Character.isDefined(c)
                || Character.getType(c) == Character.CONTROL
                || Character.getType(c) == Character.FORMAT
                || Character.getType(c) == Character.UNASSIGNED
                || Character.getType(c) == Character.SURROGATE) {
            return '.';
        }
        return c;
    }

    /**
     * Updates the layout and preferred sizes of both views.
     */
    private void updateViewsLayout() {
        if (hexView != null) {
            hexView.revalidate();
        }
        if (textView != null) {
            textView.revalidate();
        }
    }

    /**
     * Repaints both the hex and text view components.
     */
    private void repaintViews() {
        if (hexView != null) {
            hexView.repaint();
        }
        if (textView != null) {
            textView.repaint();
        }
    }

    /**
     * Ensures that the caret position is visible in both views.
     */
    private void scrollToCaret() {
        if (hexView != null) {
            hexView.scrollToCaret();
        }
        if (textView != null) {
            textView.scrollToCaret();
        }
    }

    /**
     * Inner component responsible for displaying and editing hexadecimal data.
     */
    private class HexView extends JComponent implements Scrollable {
        private boolean caretBlinkVisible = true;
        private final Timer caretTimer;

        HexView() {
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
            setOpaque(true);
            caretTimer = new Timer(500, e -> {
                caretBlinkVisible = !caretBlinkVisible;
                repaint();
            });

            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    caretBlinkVisible = true;
                    caretTimer.start();
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    caretTimer.stop();
                    caretBlinkVisible = false;
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    requestFocusInWindow();
                    handleMouseClick(e.getX(), e.getY());
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyPressed(e);
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    handleKeyTyped(e);
                }
            });
        }

        private void handleMouseClick(int mouseX, int mouseY) {
            Font font = getFont();
            if (font == null) {
                return;
            }
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            Insets insets = getInsets();

            int row = (mouseY - insets.top) / lineHeight;
            int col = (mouseX - insets.left) / charWidth;
            if (row < 0 || col < 0) {
                return;
            }

            int cpl = getCharsPerLine();
            int itemInRow = col / 5;
            int subPos = Math.min(4, col % 5);
            int itemIndex = row * cpl + itemInRow;
            int targetHexPos = itemIndex * 5 + subPos;
            setHexCaretPosition(targetHexPos);
        }

        private void handleKeyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            int cpl = getCharsPerLine();
            int len = content.length();

            switch (keyCode) {
                case KeyEvent.VK_LEFT -> {
                    setHexCaretPosition(hexCaretPosition - 1);
                    e.consume();
                }
                case KeyEvent.VK_RIGHT -> {
                    setHexCaretPosition(hexCaretPosition + 1);
                    e.consume();
                }
                case KeyEvent.VK_UP -> {
                    setHexCaretPosition(hexCaretPosition - cpl * 5);
                    e.consume();
                }
                case KeyEvent.VK_DOWN -> {
                    setHexCaretPosition(hexCaretPosition + cpl * 5);
                    e.consume();
                }
                case KeyEvent.VK_HOME -> {
                    int curItem = hexCaretPosition / 5;
                    int row = curItem / cpl;
                    setHexCaretPosition(row * cpl * 5);
                    e.consume();
                }
                case KeyEvent.VK_END -> {
                    int curItem = hexCaretPosition / 5;
                    int row = curItem / cpl;
                    int lastItemInRow = Math.min(len, (row + 1) * cpl);
                    setHexCaretPosition(lastItemInRow * 5);
                    e.consume();
                }
                case KeyEvent.VK_DELETE -> {
                    if (editable) {
                        int itemIndex = hexCaretPosition / 5;
                        if (itemIndex < len) {
                            content.deleteCharAt(itemIndex);
                            setHexCaretPosition(itemIndex * 5);
                            updateViewsLayout();
                            repaintViews();
                        }
                    }
                    e.consume();
                }
                case KeyEvent.VK_BACK_SPACE -> {
                    if (editable) {
                        int itemIndex = hexCaretPosition / 5;
                        int subPos = hexCaretPosition % 5;
                        if (subPos == 0) {
                            if (itemIndex > 0) {
                                content.deleteCharAt(itemIndex - 1);
                                setHexCaretPosition((itemIndex - 1) * 5);
                                updateViewsLayout();
                                repaintViews();
                            }
                        } else {
                            if (itemIndex < len) {
                                content.deleteCharAt(itemIndex);
                                setHexCaretPosition(itemIndex * 5);
                                updateViewsLayout();
                                repaintViews();
                            }
                        }
                    }
                    e.consume();
                }
                case KeyEvent.VK_INSERT -> {
                    if (editable) {
                        handleInsertNullCharacter();
                    }
                    e.consume();
                }
                default -> {
                }
            }
        }

        private void handleKeyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (c == KeyEvent.CHAR_UNDEFINED || e.isControlDown()
                    || e.isMetaDown() || e.isAltDown()) {
                return;
            }

            if (!editable) {
                return;
            }

            int subPos = hexCaretPosition % 5;
            int itemIndex = hexCaretPosition / 5;
            int len = content.length();

            // Insert or space key at word boundaries inserts four null digits.
            if (c == ' ') {
                if (subPos == 0 || subPos == 4 || len == 0) {
                    handleInsertNullCharacter();
                }
                e.consume();
                return;
            }

            // Check if char is a valid hex digit (0-9, A-F, a-f).
            boolean isHexDigit = (c >= '0' && c <= '9')
                    || (c >= 'a' && c <= 'f')
                    || (c >= 'A' && c <= 'F');

            if (isHexDigit) {
                if (subPos < 4) {
                    if (len == 0) {
                        content.append('\u0000');
                    }
                    if (itemIndex < content.length()) {
                        char oldChar = content.charAt(itemIndex);
                        String hexStr = getHexCode(oldChar);
                        char[] chars = hexStr.toCharArray();
                        chars[subPos] = Character.toUpperCase(c);
                        int newCode = Integer.parseInt(new String(chars), 16);
                        content.setCharAt(itemIndex, (char) newCode);
                        setHexCaretPosition(hexCaretPosition + 1);
                        updateViewsLayout();
                        repaintViews();
                    }
                }
            }
            e.consume();
        }

        private void handleInsertNullCharacter() {
            int subPos = hexCaretPosition % 5;
            int itemIndex = hexCaretPosition / 5;
            int len = content.length();

            if (len == 0 || subPos == 0) {
                content.insert(itemIndex, '\u0000');
                setHexCaretPosition(itemIndex * 5);
            } else if (subPos == 4) {
                int insertPos = Math.min(len, itemIndex + 1);
                content.insert(insertPos, '\u0000');
                setHexCaretPosition(insertPos * 5);
            }
            updateViewsLayout();
            repaintViews();
        }

        void scrollToCaret() {
            Font font = getFont();
            if (font == null) {
                return;
            }
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            int cpl = getCharsPerLine();
            int curItem = hexCaretPosition / 5;
            int curSub = hexCaretPosition % 5;
            int row = curItem / cpl;
            int col = curItem % cpl;
            Insets insets = getInsets();
            int x = insets.left + (col * 5 + curSub) * charWidth;
            int y = insets.top + row * lineHeight;
            scrollRectToVisible(new Rectangle(x, y, 4 * charWidth, lineHeight));
        }

        @Override
        public Dimension getPreferredSize() {
            Font font = getFont();
            if (font == null) {
                return new Dimension(200, 100);
            }
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            int cpl = getCharsPerLine();
            int len = content.length();
            int totalLines = len == 0 ? 1 : (len + cpl - 1) / cpl;
            Insets insets = getInsets();
            int w = (cpl * 5) * charWidth + insets.left + insets.right;
            int h = totalLines * lineHeight + insets.top + insets.bottom;
            return new Dimension(w, h);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            Font font = getFont();
            if (font == null) {
                return;
            }
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            int ascent = fm.getAscent();
            int cpl = getCharsPerLine();
            int len = content.length();
            int totalLines = len == 0 ? 1 : (len + cpl - 1) / cpl;
            Insets insets = getInsets();

            int activeCharIndex = (caretPosition < len) ? caretPosition
                    : (len > 0 && caretPosition == len ? len - 1 : -1);

            for (int row = 0; row < totalLines; row++) {
                int start = row * cpl;
                int end = Math.min(len, (row + 1) * cpl);
                int baselineY = insets.top + row * lineHeight + ascent;
                int rectY = insets.top + row * lineHeight;

                for (int i = start; i < end; i++) {
                    int col = i - start;
                    int x = insets.left + col * 5 * charWidth;
                    String hex = getHexCode(content.charAt(i));

                    if (i == activeCharIndex) {
                        g.setColor(getCaretBackground());
                        g.fillRect(x, rectY, 4 * charWidth, lineHeight);
                        g.setColor(getCaretForeground());
                    } else {
                        g.setColor(getForeground());
                    }
                    g.drawString(hex, x, baselineY);
                }
            }

            // Draw cursor caret line if focused.
            if (isFocusOwner() && caretBlinkVisible) {
                int curItem = hexCaretPosition / 5;
                int curSub = hexCaretPosition % 5;
                int curRow = curItem / cpl;
                int curCol = curItem % cpl;
                int cursorX = insets.left + (curCol * 5 + curSub) * charWidth;
                int cursorY = insets.top + curRow * lineHeight;
                g.setColor(getCaretForeground() != null ? getCaretForeground()
                        : getForeground());
                g.drawLine(cursorX, cursorY, cursorX, cursorY + lineHeight - 1);
            }
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect,
                                              int orientation, int direction) {
            Font font = getFont();
            if (font == null) {
                return 16;
            }
            FontMetrics fm = getFontMetrics(font);
            return orientation == SwingConstants.VERTICAL
                    ? Math.max(1, fm.getHeight())
                    : Math.max(1, fm.charWidth('0'));
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect,
                                               int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL
                    ? visibleRect.height : visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    /**
     * Inner component responsible for displaying and editing textual data.
     */
    private class TextView extends JComponent implements Scrollable {
        private boolean caretBlinkVisible = true;
        private final Timer caretTimer;

        TextView() {
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
            setOpaque(true);
            caretTimer = new Timer(500, e -> {
                caretBlinkVisible = !caretBlinkVisible;
                repaint();
            });

            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    caretBlinkVisible = true;
                    caretTimer.start();
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    caretTimer.stop();
                    caretBlinkVisible = false;
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    requestFocusInWindow();
                    handleMouseClick(e.getX(), e.getY());
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyPressed(e);
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    handleKeyTyped(e);
                }
            });
        }

        private void handleMouseClick(int mouseX, int mouseY) {
            Font font = getFont();
            if (font == null) {
                return;
            }
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            Insets insets = getInsets();

            int row = (mouseY - insets.top) / lineHeight;
            int col = (mouseX - insets.left) / charWidth;
            if (row < 0 || col < 0) {
                return;
            }

            int cpl = getCharsPerLine();
            int targetPos = row * cpl + col;
            int clampedPos = Math.max(0,
                    Math.min(content.length(), targetPos));
            setCaretPosition(clampedPos);
        }

        private void handleKeyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            int cpl = getCharsPerLine();
            int len = content.length();

            switch (keyCode) {
                case KeyEvent.VK_LEFT -> {
                    setCaretPosition(caretPosition - 1);
                    e.consume();
                }
                case KeyEvent.VK_RIGHT -> {
                    setCaretPosition(caretPosition + 1);
                    e.consume();
                }
                case KeyEvent.VK_UP -> {
                    setCaretPosition(caretPosition - cpl);
                    e.consume();
                }
                case KeyEvent.VK_DOWN -> {
                    setCaretPosition(caretPosition + cpl);
                    e.consume();
                }
                case KeyEvent.VK_HOME -> {
                    int row = caretPosition / cpl;
                    setCaretPosition(row * cpl);
                    e.consume();
                }
                case KeyEvent.VK_END -> {
                    int row = caretPosition / cpl;
                    int lastInRow = Math.min(len, (row + 1) * cpl);
                    setCaretPosition(lastInRow);
                    e.consume();
                }
                case KeyEvent.VK_DELETE -> {
                    if (editable && caretPosition < len) {
                        content.deleteCharAt(caretPosition);
                        setCaretPosition(caretPosition);
                        updateViewsLayout();
                        repaintViews();
                    }
                    e.consume();
                }
                case KeyEvent.VK_BACK_SPACE -> {
                    if (editable && caretPosition > 0) {
                        content.deleteCharAt(caretPosition - 1);
                        setCaretPosition(caretPosition - 1);
                        updateViewsLayout();
                        repaintViews();
                    }
                    e.consume();
                }
                default -> {
                }
            }
        }

        private void handleKeyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (c == KeyEvent.CHAR_UNDEFINED || e.isControlDown()
                    || e.isMetaDown() || e.isAltDown()) {
                return;
            }
            if (!editable) {
                return;
            }
            if (c == '\b' || c == 127) {
                return;
            }

            content.insert(caretPosition, c);
            setCaretPosition(caretPosition + 1);
            updateViewsLayout();
            repaintViews();
            e.consume();
        }

        void scrollToCaret() {
            Font font = getFont();
            if (font == null) {
                return;
            }
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            int cpl = getCharsPerLine();
            int curItem = caretPosition;
            int row = curItem / cpl;
            int col = curItem % cpl;
            Insets insets = getInsets();
            int x = insets.left + col * charWidth;
            int y = insets.top + row * lineHeight;
            scrollRectToVisible(new Rectangle(x, y, charWidth, lineHeight));
        }

        @Override
        public Dimension getPreferredSize() {
            Font font = getFont();
            if (font == null) {
                return new Dimension(200, 100);
            }
            FontMetrics fm = getFontMetrics(font);
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            int cpl = getCharsPerLine();
            int len = content.length();
            int totalLines = len == 0 ? 1 : (len + cpl - 1) / cpl;
            Insets insets = getInsets();
            int w = cpl * charWidth + insets.left + insets.right;
            int h = totalLines * lineHeight + insets.top + insets.bottom;
            return new Dimension(w, h);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            Font font = getFont();
            if (font == null) {
                return;
            }
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int lineHeight = Math.max(1, fm.getHeight());
            int charWidth = Math.max(1, fm.charWidth('0'));
            int ascent = fm.getAscent();
            int cpl = getCharsPerLine();
            int len = content.length();
            int totalLines = len == 0 ? 1 : (len + cpl - 1) / cpl;
            Insets insets = getInsets();

            int activeCharIndex = (caretPosition < len) ? caretPosition
                    : (len > 0 && caretPosition == len ? len - 1 : -1);

            for (int row = 0; row < totalLines; row++) {
                int start = row * cpl;
                int end = Math.min(len, (row + 1) * cpl);
                int baselineY = insets.top + row * lineHeight + ascent;
                int rectY = insets.top + row * lineHeight;

                for (int i = start; i < end; i++) {
                    int col = i - start;
                    int x = insets.left + col * charWidth;
                    char dispChar = getDisplayChar(content.charAt(i));

                    if (i == activeCharIndex) {
                        g.setColor(getCaretBackground());
                        g.fillRect(x, rectY, charWidth, lineHeight);
                        g.setColor(getCaretForeground());
                    } else {
                        g.setColor(getForeground());
                    }
                    g.drawString(String.valueOf(dispChar), x, baselineY);
                }
            }

            // Draw cursor caret line if focused.
            if (isFocusOwner() && caretBlinkVisible) {
                int curRow = caretPosition / cpl;
                int curCol = caretPosition % cpl;
                int cursorX = insets.left + curCol * charWidth;
                int cursorY = insets.top + curRow * lineHeight;
                g.setColor(getCaretForeground() != null ? getCaretForeground()
                        : getForeground());
                g.drawLine(cursorX, cursorY, cursorX, cursorY + lineHeight - 1);
            }
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect,
                                              int orientation, int direction) {
            Font font = getFont();
            if (font == null) {
                return 16;
            }
            FontMetrics fm = getFontMetrics(font);
            return orientation == SwingConstants.VERTICAL
                    ? Math.max(1, fm.getHeight())
                    : Math.max(1, fm.charWidth('0'));
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect,
                                               int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL
                    ? visibleRect.height : visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
