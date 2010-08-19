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

import javax.swing.table.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

public class ComplexTableMap extends AbstractTableModel implements TableModelListener
{
    protected ComplexTableModel model;
    
    public ComplexTableModel getModel()
    {
        return model;
    }
    
    public void setModel(ComplexTableModel model)
    {
        this.model = model;
        model.addTableModelListener(this);
    }
    
    // By default, implement TableModel by forwarding all messages
    // to the model.
    
    public Object getValueAt(int aRow, int aColumn)
    {
        return model.getValueAt(aRow, aColumn);
    }
    
    public void setValueAt(Object aValue, int aRow, int aColumn)
    {
        model.setValueAt(aValue, aRow, aColumn);
    }
    
    public int getRowCount()
    {
        return (model == null) ? 0 : model.getRowCount();
    }
    
    public int getColumnCount()
    {
        return (model == null) ? 0 : model.getColumnCount();
    }
    
    public String getColumnName(int aColumn)
    {
        return model.getColumnName(aColumn);
    }
    
    public Class getColumnClass(int aColumn)
    {
        return model.getColumnClass(aColumn);
    }
    
    public boolean isCellEditable(int row, int column)
    {
        return model.isCellEditable(row, column);
    }
    //
    // Implementation of the TableModelListener interface,
    //
    // By default forward all events to all the listeners.
    public void tableChanged(TableModelEvent e)
    {
        fireTableChanged(e);
    }
}
