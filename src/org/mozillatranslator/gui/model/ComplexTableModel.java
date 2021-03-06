/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is MozillaTranslator (Mozilla Localization Tool)
 *
 * The Initial Developer of the Original Code is Henrik Lynggaard Hansen
 *
 * Portions created by Henrik Lynggard Hansen are
 * Copyright (C) Henrik Lynggaard Hansen.
 * All Rights Reserved.
 *
 * Contributor(s):
 * Henrik Lynggaard Hansen (Initial Code)
 *
 */
package org.mozillatranslator.gui.model;

import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.gui.ThreeDotKeyAdapter;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class ComplexTableModel extends AbstractTableModel {

    private List<Phrase> phrases;
    private int rowCount;
    private List<TableColumn> columns;
    private int columnCount;
    private String currentLocalization;

    /* Somehow, now we need a reference to the JTable to keep track of current
     * rows when a change happens */
    private JTable jTableReference;

    /** Creates new ComplexTableModel
     * @param p List of phrases displayed in the table as rows
     * @param c List of columns displayed in the table
     * @param l locale code */
    public ComplexTableModel(List<Phrase> p, List<TableColumn> c, String l) {
        init(p, c, l);
    }

    public final void init(List<Phrase> p, List<TableColumn> c, String l) {
        phrases = p;
        columns = c;
        currentLocalization = l;
        rowCount = phrases.size();
        columnCount = columns.size();
    }
    /*
     * Methods for AbstractTableModel
     */

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    public TableColumnModel getTableColumnModel() {
        DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
        TableColumn column;

        Iterator<TableColumn> it = columns.iterator();
        int i = 0;
        while (it.hasNext()) {
            ComplexColumn currentColumn = (ComplexColumn) it.next();
            column = new TableColumn(i++);
            column.setHeaderValue(currentColumn.getColumnName());
            column.setPreferredWidth(currentColumn.getPreferredWidth());

            if (currentColumn instanceof TranslatedTextColumn) {
                JTextField textField = new JTextField();
                textField.addKeyListener(new ThreeDotKeyAdapter());
                DefaultCellEditor dce = new DefaultCellEditor(textField);
                column.setCellEditor(dce);
            }

            columnModel.addColumn(column);
        }
        return columnModel;
    }

    @Override
    public String getColumnName(int index) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(index);

        return currentColumn.getColumnName();
    }

    @Override
    public Class<? extends Object> getColumnClass(int index) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(index);

        return currentColumn.getColumnClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(columnIndex);
        Phrase currentRow = phrases.get(rowIndex);

        return currentColumn.isCellEditable(currentRow, currentLocalization);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(columnIndex);
        Phrase currentRow;
        try {
            currentRow = phrases.get(rowIndex);
            return currentColumn.getValue(currentRow, currentLocalization);
        } catch (RuntimeException e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        int curjTableRow;
        int curjTableCol;

        ComplexColumn currentColumn = (ComplexColumn) columns.get(columnIndex);
        Phrase currentRow = phrases.get(rowIndex);

        currentColumn.setValue(currentRow, aValue, currentLocalization);
        curjTableRow = this.getJTableReference().getSelectedRow();
        curjTableCol = this.getJTableReference().getSelectedColumn();
        this.fireTableDataChanged();
        this.getJTableReference().setRowSelectionInterval(curjTableRow, curjTableRow);
        this.getJTableReference().setColumnSelectionInterval(curjTableCol, curjTableCol);
    }

    public String getLocalization() {
        return currentLocalization;
    }

    public Phrase getRow(int index) {
        return phrases.get(index);
    }

    public JTable getJTableReference() {
        return jTableReference;
    }

    public void setJTableReference(JTable jTableReference) {
        this.jTableReference = jTableReference;
    }
}
