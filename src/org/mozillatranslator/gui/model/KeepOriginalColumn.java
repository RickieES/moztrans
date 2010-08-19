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

import javax.swing.*;

import org.mozillatranslator.datamodel.*;
/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class KeepOriginalColumn implements ComplexColumn {
    private static final Class BOOL_CLASS = Boolean.TRUE.getClass();
    
    /** Creates new KeyColumn */
    public KeepOriginalColumn()
    {
        // do Noting
    }
    
    public Class getColumnClass()
    {
        return BOOL_CLASS;
    }
    
    public Object getValue(Phrase currentPhrase, String currentLocalization)
    {
        return Boolean.valueOf(currentPhrase.isKeepOriginal());
    }
    
    public boolean isCellEditable(Phrase currentPhrase, String currentLocalization)
    {
        return true;
    }
    
    public String getColumnName()
    {
        return "Keep";
    }
    
    public void setValue(Phrase currentPhrase, Object value, String currentLocalization)
    {
        Boolean boolValue = (Boolean) value;
        currentPhrase.setKeepOriginal(boolValue.booleanValue());
    }
    
    public String toString()
    {
        return "Keep original";
    }
    
    public void init(JTable table)
    {
    }
    public int getPrefferedWidth()
    {
        return 50;
    }
}
