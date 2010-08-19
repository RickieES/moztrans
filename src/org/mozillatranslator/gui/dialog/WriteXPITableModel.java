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


package org.mozillatranslator.gui.dialog;

import javax.swing.JOptionPane;
import javax.swing.table.*;
import java.util.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class WriteXPITableModel extends AbstractTableModel {
    public static final int COLUMN_PRODUCT  = 0;
    public static final int COLUMN_VERSION  = 1;
    public static final int COLUMN_NEUTRAL  = 2;
    public static final int COLUMN_WINDOWS  = 3;
    public static final int COLUMN_UNIX     = 4;
    public static final int COLUMN_MAC      = 5;
    public static final int COLUMN_REGION   = 6;
    public static final int COLUMN_CUSTOM   = 7;
    public static final int COLUMN_CHECKALL = 8;
    public static final int COLUMN_MAX      = 9; // Total number of columns
    
    private List rows = new ArrayList();
    private int rowCount;
    private WriteXPIDialog theDialog;

    /** Creates new WriteXPITableModel */
    public WriteXPITableModel(WriteXPIDialog theDialog) {
        this.theDialog = theDialog;
    }

    public void loadProducts() {
        rows.clear();

        Iterator productIterator = Kernel.datamodel.productIterator();
        int i = 0;
        while (productIterator.hasNext()) {
            Product currentProduct = (Product) productIterator.next();
            rows.add(new WriteXPITableModelRow(currentProduct, (i == 0)));
            i++;
        }
        rowCount = rows.size();
    }

    public String getColumnName(int index) {
        String result = "";

        switch (index) {
            case COLUMN_PRODUCT:
                result = "Product";
                break;
            case COLUMN_VERSION:
                result = "Version";
                break;
            case COLUMN_NEUTRAL:
                result = "Neutral";
                break;
            case COLUMN_WINDOWS:
                result = "Windows";
                break;
            case COLUMN_UNIX:
                result = "Unix";
                break;
            case COLUMN_MAC:
                result = "Mac";
                break;
            case COLUMN_REGION:
                result = "Region";
                break;
            case COLUMN_CUSTOM:
                result = "Custom";
                break;
            case COLUMN_CHECKALL:
                result = "Check All";
                break;
        }
        return result;
    }

    public Class getColumnClass(int index) {
        Class result;

        if (index == COLUMN_PRODUCT || index == COLUMN_VERSION) {
            result = String.class;
        } else {
            result = Boolean.class;
        }
        return result;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Product and version are not editable
        return ((columnIndex != COLUMN_PRODUCT) && (columnIndex != COLUMN_VERSION));
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return COLUMN_MAX;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        WriteXPITableModelRow node = (WriteXPITableModelRow) rows.get(rowIndex);
        
        switch (columnIndex) {
            case COLUMN_PRODUCT:
                result = node.getProduct();
                break;
            case COLUMN_VERSION:
                result = node.getVersion();
                break;
            case COLUMN_NEUTRAL:
                result = Boolean.valueOf(node.isNeutral());
                break;
            case COLUMN_WINDOWS:
                result = Boolean.valueOf(node.isWindows());
                break;
            case COLUMN_UNIX:
                result =  Boolean.valueOf(node.isUnix());
                break;
            case COLUMN_MAC:
                result = Boolean.valueOf(node.isMac());
                break;
            case COLUMN_REGION:
                result = Boolean.valueOf(node.isRegion());
                break;
            case COLUMN_CUSTOM:
                result = Boolean.valueOf(node.isCustom());
                break;
            case COLUMN_CHECKALL:
                result = Boolean.valueOf(node.isCheckall());
        }
        return result;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Boolean boolValue = (Boolean) aValue;
        WriteXPITableModelRow node = (WriteXPITableModelRow) rows.get(rowIndex);

        if ((!theDialog.getVersion().equals(node.getVersion())) &&
                (!theDialog.getVersion().equals(""))) {
            theDialog.reportVersionConflict();
            return;
        }
        
        
        switch (columnIndex) {
            case COLUMN_NEUTRAL:
                node.setNeutral(boolValue.booleanValue());
                break;
            case COLUMN_WINDOWS:
                node.setWindows(boolValue.booleanValue());
                break;
            case COLUMN_UNIX:
                node.setUnix(boolValue.booleanValue());
                break;
            case COLUMN_MAC:
                node.setMac(boolValue.booleanValue());
                break;
            case COLUMN_REGION:
                node.setRegion(boolValue.booleanValue());
                break;
            case COLUMN_CUSTOM:
                node.setCustom(boolValue.booleanValue());
                break;
            case COLUMN_CHECKALL:
                node.setCheckall(boolValue.booleanValue());
                node.setCustom(boolValue.booleanValue());
                node.setMac(boolValue.booleanValue());
                node.setNeutral(boolValue.booleanValue());
                node.setRegion(boolValue.booleanValue());
                node.setUnix(boolValue.booleanValue());
                node.setWindows(boolValue.booleanValue());
                this.fireTableRowsUpdated(rowIndex, rowIndex);
                break;
        }

        if (!boolValue.booleanValue()) {
            node.setCheckall(false);
            this.fireTableCellUpdated(rowIndex, this.COLUMN_CHECKALL);
        }
        
        theDialog.setVersion(this.getCurrentVersion());
    }

    public Iterator iterator() {
        return rows.iterator();
    }
    
    public String getCurrentVersion() {
        String currentVersion = null;
        Iterator it = this.iterator();
        WriteXPITableModelRow curRow;
        
        while (it.hasNext() && currentVersion == null) {
            curRow = (WriteXPITableModelRow) it.next();
            if (curRow.isNeutral() || curRow.isWindows() || curRow.isUnix() ||
                    curRow.isMac() || curRow.isRegion() || curRow.isCustom()) {
                currentVersion = curRow.getVersion();
            }
        }
        
        if (currentVersion == null) {
            currentVersion = "";
        }

        return currentVersion;
    }
}
