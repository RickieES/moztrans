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
 * Portions created by Ricardo Palomares are
 * Copyright (C) Ricardo Palomares.
 * All Rights Reserved.
 *
 * Contributor(s):
 * Ricardo Palomares (Initial Code)
 *
 */

package org.mozillatranslator.gui.model;

import javax.swing.table.AbstractTableModel;
import org.mozillatranslator.datamodel.AutoAccessKeyAssign.AccessKeyBundle;
import org.mozillatranslator.datamodel.AutoAccessKeyAssign.AccessKeyBundleList;
import org.mozillatranslator.datamodel.Phrase;

/**
 * Table model for the accesskey auto-assign feature
 * @author rpalomares
 */
public class AutoAccessKeyTableModel extends AbstractTableModel {
    private AccessKeyBundleList data;
    private static final String[] columnNames = {"Accesskey name",
                                                 "Original value",
                                                 "Associated label value",
                                                 "Fuzzy state (copy)",
                                                 "Current value",
                                                 "Proposed value"};
    private static final int[] columnWidths = {60, 20, 150, 30, 20, 20};
    
    /**
     * Creates the table model with the link to the data to show
     * @param data an AccessKeyBundleList object containing the data to show
     */
    public AutoAccessKeyTableModel(AccessKeyBundleList data) {
        super();
        this.data = data;
    }

    @Override public int getRowCount() {
        return data.size();
    }

    @Override public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    /**
     * Returns the preferred column widths
     * @param index the column index whose preferred width we want to know
     * @return the preferred index for the column[idx], or 50 for unknown columns
     */
    public int getColumnDefaultWidth(int index) {
        if ((index >=0) && (index <= columnWidths.length)) {
            return columnWidths[index];
        } else {
            return 50;
        }
    }
    
    @Override
    public String getColumnName(int index) {
        if ((index >=0) && (index <= columnNames.length)) {
            return columnNames[index];
        } else {
            return "";
        }
    }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        AccessKeyBundle item = data.get(rowIndex);
        switch (columnIndex) {
            case 0: // Accesskey name
                    return item.getOriginal().getName();
            case 1: // Original value
                    return item.getOriginal().getText();
            case 2: // Associated label value
                    if (item.getBoundLabel() != null) {
                        return item.getBoundLabel().getText();
                    } else {
                        if (item.getOriginal().getLabelConnection() != null) {
                            Phrase label = item.getOriginal().getLabelConnection();
                            return label.getText();
                        } else {
                            return "<no bound label>";
                        }
                    }
            case 3: // Fuzzy state (copy)
                    return item.isFuzzy();
            case 4: // Current value
                    if (item.getAKeyItem() != null) {
                        return item.getAKeyItem().getText();
                    } else {
                        return item.getOriginal().getText();
                    }
            case 5: // Proposed value
                    return data.get(rowIndex).getProposedValue();
            default: return null;
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        if ((col == 3) || (col == 5)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        switch (col) {
            case 3: data.get(row).setFuzzy(Boolean.valueOf(value.toString()));
                    break;
            case 5: try {
                        data.get(row).setProposedValue(value.toString().substring(0, 1));
                    } catch (NullPointerException e) {
                        data.get(row).setProposedValue("");
                    }
                    break;
            default:
        }
        fireTableCellUpdated(row, col);
    }    
}
