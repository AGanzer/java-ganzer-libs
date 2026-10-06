package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.MDISubView;
import com.formdev.flatlaf.ui.FlatUIUtils;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class CSVView<D extends CSVDocument> extends MDISubView<D> {
    private final JXTable table;

    public CSVView(ViewCreationInfo<D, CSVView<D>> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        var rowHeader = new RowHeader();

        table = new CSVTable(rowHeader);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeader);
        add(scrollPane, BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void documentDataChanged(Object context) {
        if (context instanceof CSVDocument.ChangeContext c)
            ((CSVTableModel) table.getModel()).fireTableCellUpdated(c.row, c.column);
        else
            ((CSVTableModel) table.getModel()).fireTableDataChanged();
    }

    @Override
    protected void focusContent() {
        SwingUtilities.invokeLater(table::requestFocusInWindow);
    }

    private class CSVTable extends JXTable {
        private final RowHeader rowHeader;

        public CSVTable(RowHeader rowHeader) {
            this.rowHeader = rowHeader;

            setModel(new CSVTableModel());
            setDefaultRenderer(String.class, new CellRenderer(this));
            setDefaultEditor(String.class, new CellEditor(this));
            getTableHeader().setDefaultRenderer(new ColumnHeaderCellRenderer());
            setShowGrid(true);
            setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
            setCellSelectionEnabled(true);
        }

        @Override
        public void setRowHeight(int row, int rowHeight) {
            super.setRowHeight(row, rowHeight);

            if (rowHeader != null) {
                rowHeader.revalidate();
                rowHeader.repaint();
            }
        }

        @Override
        public void setRowHeight(int rowHeight) {
            super.setRowHeight(rowHeight);

            if (rowHeader != null) {
                rowHeader.revalidate();
                rowHeader.repaint();
            }
        }
    }

    private class CSVTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return Math.max(256, getDocument().getRowCount());
        }

        @Override
        public int getColumnCount() {
            return Math.max(256, getDocument().getColumnCount());
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return !getDocument().isReadOnly();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return getDocument().getValue(rowIndex, columnIndex);
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            getDocument().setValue((String) value, rowIndex, columnIndex, CSVView.this);
        }
    }

    private static class CellRenderer extends DefaultTableCellRenderer {
        private final JTable table;

        private CellRenderer(JTable table) {
            this.table = table;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            updateRowHeight(row);

            return this;
        }

        @Override
        protected void setValue(Object value) {
            String text = (String) value;

            if (text == null)
                setText("");
            else {
                text = text.replace("\n", "<br>").replace("\r", "");
                setText(String.format("<html>%s</html>", text));
            }
        }

        private void updateRowHeight(int row) {
            int preferredHeight = getPreferredSize().height;

            if (table.getRowHeight(row) < preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }
        }
    }

    private static class CellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JTextArea textArea;
        private final JTable table;
        private int currentRow = -1;

        public CellEditor(JTable table) {
            this.table = table;
            textArea = new JTextArea();

            InputMap im = textArea.getInputMap(WHEN_FOCUSED);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "transferFocus");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK), "transferFocusBackward");

            ActionMap am = textArea.getActionMap();
            am.put("transferFocus", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopCellEditing();
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                }
            });
            am.put("transferFocusBackward", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopCellEditing();
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                }
            });

            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    updateRowHeight();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    updateRowHeight();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    updateRowHeight();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            String text = (String) value;
            textArea.setText(text == null ? "" : text);

            updateRowHeight();

            return textArea;
        }

        @Override
        public Object getCellEditorValue() {
            return textArea.getText();
        }

        private void updateRowHeight() {
            int preferredHeight = textArea.getPreferredSize().height;

            if (table.getRowHeight(currentRow) < preferredHeight) {
                table.setRowHeight(currentRow, preferredHeight);
            }
        }
    }

    private static class ColumnHeaderCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setHorizontalAlignment(SwingConstants.CENTER);
            if (column >= 0)
                setText(toExcelColumnName(column));

            Border border = UIManager.getBorder("TableHeader.cellBorder");

            if (border != null)
                setBorder(border);

            return c;
        }

        private static String toExcelColumnName(int column) {
            StringBuilder result = new StringBuilder();

            column++;

            while (column > 0) {
                int remainder = (column - 1) % 26;
                result.insert(0, (char) ('A' + remainder));

                column = (column - 1) / 26;
            }

            return result.toString();
        }
    }

    private class RowHeader extends JComponent {
        private int hoverRow = -1;
        private int pressedRow = -1;

        public RowHeader() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateHoverRow(e.getPoint());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoverRow = -1;
                    pressedRow = -1;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        pressedRow = row;
                        hoverRow = row;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressedRow = -1;
                    updateHoverRow(e.getPoint());
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    updateHoverRow(e.getPoint());
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    updateHoverRow(e.getPoint());
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(45, table.getPreferredSize().height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            var visible = g.getClipBounds();

            JTableHeader header = table.getTableHeader();
            g.setColor(header.getBackground());
            g.fillRect(visible.x, visible.y, visible.width, visible.height);

            int firstRow = table.rowAtPoint(new Point(0, visible.y));

            if (firstRow < 0)
                firstRow = 0;

            int lastRow = table.rowAtPoint(new Point(0, visible.y + visible.height - 1));

            if (lastRow < 0)
                lastRow = table.getRowCount() - 1;

            TableCellRenderer renderer = header.getDefaultRenderer();

            Color hoverBackground = UIManager.getColor("TableHeader.hoverBackground");
            Color hoverForeground = UIManager.getColor("TableHeader.hoverForeground");
            Color pressedBackground = UIManager.getColor("TableHeader.pressedBackground");
            Color pressedForeground = UIManager.getColor("TableHeader.pressedForeground");

            for (int row = firstRow; row <= lastRow; row++) {
                int y = table.getCellRect(row, 0, true).y;
                int height = table.getRowHeight(row);
                var text = Integer.toString(table.convertRowIndexToModel(row) + 1);

                Component component = renderer.getTableCellRendererComponent(table, text, false, false, row, -1);

                if (component instanceof JLabel label) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setText(text);

                    Color bg = header.getBackground();
                    Color fg = header.getForeground();
                    label.setFont(header.getFont());

                    if (row == pressedRow) {
                        if (pressedBackground != null) {
                            label.setBackground(FlatUIUtils.deriveColor(pressedBackground, bg));
                            label.setOpaque(true);
                        }

                        if (pressedForeground != null)
                            label.setForeground(FlatUIUtils.deriveColor(pressedForeground, fg));
                    } else if (row == hoverRow) {
                        if (hoverBackground != null) {
                            label.setBackground(FlatUIUtils.deriveColor(hoverBackground, bg));
                            label.setOpaque(true);
                        }

                        if (hoverForeground != null)
                            label.setForeground(FlatUIUtils.deriveColor(hoverForeground, fg));
                    } else {
                        label.setBackground(bg);
                        label.setForeground(fg);
                    }
                }

                SwingUtilities.paintComponent(g, component, this, 0, y, getWidth(), height);
            }
        }

        private void updateHoverRow(Point point) {
            int row = table.rowAtPoint(point);

            if (row < 0 || row >= table.getRowCount())
                row = -1;

            if (hoverRow != row) {
                hoverRow = row;
                repaint();
            }
        }
    }
}
