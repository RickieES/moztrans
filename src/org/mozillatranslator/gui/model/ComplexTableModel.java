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

import java.util.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.*;

import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.gui.ThreeDotKeyAdapter;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class ComplexTableModel extends AbstractTableModel {

    private List phrases;
    private int rowCount;
    private List columns;
    private int columnCount;
    private String currentLocalization;

    /* Somehow, now we need a reference to the JTable to keep track of current
     * rows when a change happens */
    private JTable jTableReference;

    /** Creates new ComplexTableModel */
    public ComplexTableModel(List p, List c, String l) {
        init(p, c, l);
    }

    public void init(List p, List c, String l) {
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
        TableColumn column = new TableColumn();

        Iterator it = columns.iterator();
        int i = 0;
        while (it.hasNext()) {
            ComplexColumn currentColumn = (ComplexColumn) it.next();
            column = new TableColumn(i++);
            column.setHeaderValue(currentColumn.getColumnName());
            column.setPreferredWidth(currentColumn.getPrefferedWidth());
            //        column.setMinWidth(30);

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
    public Class getColumnClass(int index) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(index);

        return currentColumn.getColumnClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(columnIndex);
        Phrase currentRow = (Phrase) phrases.get(rowIndex);

        return currentColumn.isCellEditable(currentRow, currentLocalization);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ComplexColumn currentColumn = (ComplexColumn) columns.get(columnIndex);
        Phrase currentRow;
        try {
            currentRow = (Phrase) phrases.get(rowIndex);
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
        Phrase currentRow = (Phrase) phrases.get(rowIndex);

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
        return (Phrase) phrases.get(index);
    }

    public JTable getJTableReference() {
        return jTableReference;
    }

    public void setJTableReference(JTable jTableReference) {
        this.jTableReference = jTableReference;
    }
}
