package com.example.uitests.swingdv.doc.db;

import com.example.uitests.swingdv.doc.MDISubView;
import com.example.uitests.swingdv.doc.text.CSVDocument;
import de.ganzer.core.validation.DateValidator;
import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionGroup;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.dv.DVManager;
import de.ganzer.swing.validaton.ValidationBehavior;
import de.ganzer.swing.validaton.ValidationFilter;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PersonView extends MDISubView<PersonDocument> {
    private final JXTable table;

    public PersonView(ViewCreationInfo<PersonDocument, ? extends View<PersonDocument>> info, ClosableTabsPane tabPane) {
        super(info, tabPane, true);

        table = new JXTable(new PersonTableModel());
        table.setShowGrid(true);
        table.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        table.setModel(new PersonTableModel());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 || e.isPopupTrigger())
                    showTableContextMenu();
            }
        });

        table.getColumnModel().getColumn(0).setCellEditor(
                new ValidatedEditor(new Validator()));
        table.getColumnModel().getColumn(1).setCellEditor(
                new ValidatedEditor(new DateValidator(LocalDate.of(1900, 1, 1), LocalDate.now(), "yyyy-MM-dd")));
        table.getColumnModel().getColumn(2).setCellEditor(
                new ValidatedEditor(new NumberValidator(0.0, 1_000_000.0, 2)));
        JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);
    }

    @Override
    public void documentDataChanged(Object context) {
        if (context instanceof CSVDocument.ChangeContext c)
            ((PersonTableModel) table.getModel()).fireTableCellUpdated(c.row, c.column);
        else
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
    }

    @Override
    protected void focusContent() {
        SwingUtilities.invokeLater(table::requestFocusInWindow);
    }

    private void showTableContextMenu() {
        var menu = new JPopupMenu();
        var actions = new GActionGroup().addAll(
                new GAction("Add Person")
                        .onAction(e -> getDocument().newPerson(null)),
                new GAction("Open as CSV")
                        .onAction(e -> {
                            var tpl = getDocument().getTemplate().getViewTemplates().stream()
                                    .filter(t -> t.getDisplayName().equals("CSV"))
                                    .findFirst()
                                    .orElse(null);
                            DVManager.createView(getDocument(), tpl);
                        })
        );
        actions.addMenuItems(menu);

        menu.show(table, table.getMousePosition().x, table.getMousePosition().y);
    }

    private class PersonTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return getDocument().getRowCount();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> getDocument().getPersonName(rowIndex);
                case 1 -> getDocument().getPersonBirthday(rowIndex);
                case 2 -> getDocument().getPersonSalary(rowIndex);
                default -> "";
            };
        }

        @Override
        public String getColumnName(int column) {
            return switch (column) {
                case 0 -> "Name";
                case 1 -> "Birthday";
                case 2 -> "Salary";
                default -> "";
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            var val = (String) value;

            switch (columnIndex) {
                case 0 -> getDocument().setPersonName(rowIndex, val, PersonView.this);
                case 1 -> getDocument().setPersonBirthday(rowIndex, LocalDate.parse(val), PersonView.this);
                case 2 -> getDocument().setPersonSalary(rowIndex, BigDecimal.valueOf(Double.parseDouble(val)), PersonView.this);
            }
        }
    }

    private static class ValidatedEditor extends AbstractCellEditor implements TableCellEditor {
        private final JTextField component;
        private final ValidationFilter filter;

        private ValidatedEditor(Validator validator) {
            component = new JTextField();
            this.filter = new ValidationFilter(validator, component, false, true);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return component;
        }

        @Override
        public boolean stopCellEditing() {
            if (filter.validate(ValidationBehavior.SET_VISUAL_HINTS))
                return super.stopCellEditing();

            return false;
        }

        @Override
        public Object getCellEditorValue() {
            return component.getText();
        }
    }
}
