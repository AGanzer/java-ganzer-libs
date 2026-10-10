package de.ganzer.swing.controls;

import org.junit.jupiter.api.Test;

import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class HexEditorTest {

    @Test
    void testInitialStateAndText() {
        var editor = new HexEditor("Hello");
        assertEquals("Hello", editor.getText());
        assertEquals(0, editor.getCaretPosition());
        assertEquals(0, editor.getHexCaretPosition());
        assertTrue(editor.isEditable());
        assertNotNull(editor.getHexView());
        assertNotNull(editor.getTextView());
        assertNotNull(editor.getHexScrollPane());
        assertNotNull(editor.getTextScrollPane());

        editor.setText("World");
        assertEquals("World", editor.getText());
        assertEquals(0, editor.getCaretPosition());
    }

    @Test
    void testEmptyConstructor() {
        var editor = new HexEditor();
        assertEquals("", editor.getText());
        assertEquals(0, editor.getCaretPosition());
    }

    @Test
    void testFontProperty() {
        var editor = new HexEditor("A");
        Font customFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
        editor.setFont(customFont);

        assertEquals(customFont, editor.getFont());
        assertEquals(customFont, editor.getHexView().getFont());
        assertEquals(customFont, editor.getTextView().getFont());
    }

    @Test
    void testColorsAndAutomaticInversion() {
        var editor = new HexEditor("A");
        Color fg = new Color(10, 20, 30);
        Color bg = new Color(200, 210, 220);

        editor.setForeground(fg);
        editor.setBackground(bg);

        assertEquals(fg, editor.getForeground());
        assertEquals(bg, editor.getBackground());
        assertEquals(fg, editor.getHexView().getForeground());
        assertEquals(bg, editor.getHexView().getBackground());
        assertEquals(fg, editor.getTextView().getForeground());
        assertEquals(bg, editor.getTextView().getBackground());

        // Inverted colors:
        Color expectedCaretFg = new Color(255 - 10, 255 - 20, 255 - 30);
        Color expectedCaretBg = new Color(255 - 200, 255 - 210, 255 - 220);
        assertEquals(expectedCaretFg, editor.getCaretForeground());
        assertEquals(expectedCaretBg, editor.getCaretBackground());

        // Explicitly overriding caret colors:
        Color customCaretFg = Color.YELLOW;
        Color customCaretBg = Color.BLUE;
        editor.setCaretForeground(customCaretFg);
        editor.setCaretBackground(customCaretBg);

        assertEquals(customCaretFg, editor.getCaretForeground());
        assertEquals(customCaretBg, editor.getCaretBackground());

        // Changing editor foreground should not overwrite explicit
        // caret foreground:
        editor.setForeground(Color.RED);
        assertEquals(customCaretFg, editor.getCaretForeground());
    }

    @Test
    void testHexCodeFormatting() {
        assertEquals("0041", HexEditor.getHexCode('A'));
        assertEquals("0000", HexEditor.getHexCode('\0'));
        assertEquals("000A", HexEditor.getHexCode('\n'));
        assertEquals("0020", HexEditor.getHexCode(' '));
        assertEquals("00E4", HexEditor.getHexCode('ä'));
    }

    @Test
    void testDisplayCharMapping() {
        // Printable ASCII
        assertEquals('A', HexEditor.getDisplayChar('A'));
        assertEquals('1', HexEditor.getDisplayChar('1'));
        assertEquals('#', HexEditor.getDisplayChar('#'));

        // Spaces and control characters mapped to '.'
        assertEquals('.', HexEditor.getDisplayChar(' '));
        assertEquals('.', HexEditor.getDisplayChar('\t'));
        assertEquals('.', HexEditor.getDisplayChar('\n'));
        assertEquals('.', HexEditor.getDisplayChar('\r'));
        assertEquals('.', HexEditor.getDisplayChar('\0'));
        assertEquals('.', HexEditor.getDisplayChar((char) 27)); // Escape
    }

    @Test
    void testScrollPaneConfigurationAndSync() {
        var editor = new HexEditor("Testing");
        assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER,
                editor.getHexScrollPane().getHorizontalScrollBarPolicy());
        assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,
                editor.getTextScrollPane().getHorizontalScrollBarPolicy());

        // Vertical scrollbars share the same model.
        assertSame(editor.getHexScrollPane().getVerticalScrollBar().getModel(),
                editor.getTextScrollPane().getVerticalScrollBar().getModel());
    }

    @Test
    void testCaretMovementAndSync() {
        var editor = new HexEditor("ABCDE");
        editor.setCaretPosition(2);
        assertEquals(2, editor.getCaretPosition());
        assertEquals(10, editor.getHexCaretPosition());

        editor.setHexCaretPosition(12);
        assertEquals(12, editor.getHexCaretPosition());
        assertEquals(2, editor.getCaretPosition());

        editor.setHexCaretPosition(15);
        assertEquals(15, editor.getHexCaretPosition());
        assertEquals(3, editor.getCaretPosition());
    }

    @Test
    void testHexViewTypingOverwritesDigit() {
        var editor = new HexEditor("A"); // 'A' = 0041
        var hexView = editor.getHexView();

        // Caret at pos 0 (digit 0 of 'A')
        editor.setHexCaretPosition(0);

        // Type '4' to replace digit 0
        KeyEvent keyEvent = new KeyEvent(hexView, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, '4');
        hexView.getKeyListeners()[0].keyTyped(keyEvent);

        // Pos was 0, so "0041" with digit 0 replaced by '4' becomes '䁁'
        assertEquals("䁁", editor.getText());
        assertEquals(1, editor.getHexCaretPosition());
    }

    @Test
    void testHexViewDeleteRemovesEntireCharacter() {
        var editor = new HexEditor("ABC");
        var hexView = editor.getHexView();
        editor.setHexCaretPosition(5); // At 'B'

        KeyEvent delEvent = new KeyEvent(hexView, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_DELETE,
                KeyEvent.CHAR_UNDEFINED);
        hexView.getKeyListeners()[0].keyPressed(delEvent);

        assertEquals("AC", editor.getText());
        assertEquals(5, editor.getHexCaretPosition());
        assertEquals(1, editor.getCaretPosition());
    }

    @Test
    void testHexViewBackspaceRemovesEntireCharacter() {
        var editor = new HexEditor("ABC");
        var hexView = editor.getHexView();
        editor.setHexCaretPosition(5); // At start of 'B'

        // Backspace at subPos 0 removes previous character ('A')
        KeyEvent bsEvent = new KeyEvent(hexView, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE,
                KeyEvent.CHAR_UNDEFINED);
        hexView.getKeyListeners()[0].keyPressed(bsEvent);

        assertEquals("BC", editor.getText());
        assertEquals(0, editor.getHexCaretPosition());
        assertEquals(0, editor.getCaretPosition());
    }

    @Test
    void testHexViewInsertAndSpaceInsertNullChar() {
        var editor = new HexEditor("AB");
        var hexView = editor.getHexView();

        // At subPos 0 of 'A' (hex pos 0)
        editor.setHexCaretPosition(0);
        KeyEvent insEvent = new KeyEvent(hexView, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_INSERT,
                KeyEvent.CHAR_UNDEFINED);
        hexView.getKeyListeners()[0].keyPressed(insEvent);

        assertEquals("\u0000AB", editor.getText());
        assertEquals(0, editor.getHexCaretPosition());

        // On the separating space between \u0000 and A (hex pos 4)
        editor.setHexCaretPosition(4);
        KeyEvent spaceEvent = new KeyEvent(hexView, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, ' ');
        hexView.getKeyListeners()[0].keyTyped(spaceEvent);

        assertEquals("\u0000\u0000AB", editor.getText());
        assertEquals(5, editor.getHexCaretPosition());
    }

    @Test
    void testHexViewIgnoresNonHexChars() {
        var editor = new HexEditor("A");
        var hexView = editor.getHexView();
        editor.setHexCaretPosition(1);

        KeyEvent invalidChar = new KeyEvent(hexView, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'Z');
        hexView.getKeyListeners()[0].keyTyped(invalidChar);

        assertEquals("A", editor.getText());
        assertEquals(1, editor.getHexCaretPosition());
    }

    @Test
    void testTextViewTypingAndEditing() {
        var editor = new HexEditor("Hi");
        var textView = editor.getTextView();
        editor.setCaretPosition(2);

        // Type '!'
        KeyEvent typeChar = new KeyEvent(textView, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, '!');
        textView.getKeyListeners()[0].keyTyped(typeChar);

        assertEquals("Hi!", editor.getText());
        assertEquals(3, editor.getCaretPosition());
        assertEquals(15, editor.getHexCaretPosition());

        // Backspace in text view
        KeyEvent bs = new KeyEvent(textView, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE,
                KeyEvent.CHAR_UNDEFINED);
        textView.getKeyListeners()[0].keyPressed(bs);

        assertEquals("Hi", editor.getText());
        assertEquals(2, editor.getCaretPosition());

        // Delete in text view
        editor.setCaretPosition(0);
        KeyEvent del = new KeyEvent(textView, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_DELETE,
                KeyEvent.CHAR_UNDEFINED);
        textView.getKeyListeners()[0].keyPressed(del);

        assertEquals("i", editor.getText());
        assertEquals(0, editor.getCaretPosition());
    }

    @Test
    void testEditableProperty() {
        var editor = new HexEditor("Test");
        editor.setEditable(false);

        var hexView = editor.getHexView();
        var textView = editor.getTextView();

        // Try typing in hex view
        KeyEvent hexType = new KeyEvent(hexView, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, '0');
        hexView.getKeyListeners()[0].keyTyped(hexType);
        assertEquals("Test", editor.getText());

        // Try typing in text view
        KeyEvent textType = new KeyEvent(textView, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'X');
        textView.getKeyListeners()[0].keyTyped(textType);
        assertEquals("Test", editor.getText());
    }

    @Test
    void testGetAndSetDocument() throws BadLocationException {
        var editor = new HexEditor("Hello");
        var doc = editor.getDocument();
        assertNotNull(doc);
        assertEquals("Hello", editor.getText());

        // Modify document directly
        doc.insertString(5, " World", null);
        assertEquals("Hello World", editor.getText());

        doc.remove(0, 6);
        assertEquals("World", editor.getText());

        // Set a new document
        var newDoc = new PlainDocument();
        newDoc.insertString(0, "Swing", null);
        editor.setDocument(newDoc);

        assertSame(newDoc, editor.getDocument());
        assertEquals("Swing", editor.getText());
        assertEquals(0, editor.getCaretPosition());

        // Set null document resets to empty PlainDocument
        editor.setDocument(null);
        assertNotNull(editor.getDocument());
        assertEquals("", editor.getText());
        assertEquals(0, editor.getCaretPosition());
    }
}
