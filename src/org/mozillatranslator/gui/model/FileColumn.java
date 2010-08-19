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
public class FileColumn implements ComplexColumn
{
    
    private static final Class STR_CLASS = "dummy".getClass();
    /** Creates new KeyColumn */
    public FileColumn()
    {
        // do Noting
    }
    
    public Class getColumnClass()
    {
        return STR_CLASS;
    }
    
    public Object getValue(Phrase currentPhrase, String currentLocalization)
    {
        return currentPhrase.getParent().getName();
    }
    
    public boolean isCellEditable(Phrase currentPhrase, String currentLocalization)
    {
        return false;
    }
    
    public String getColumnName()
    {
        return "File";
    }
    
    public void setValue(Phrase currentPhrase, Object value, String currentLocalization)
    {
        // non editable
    }
    
    public String toString()
    {
        return "File";
    }
    
    public void init(JTable table)
    {
    }
    
    public int getPrefferedWidth()
    {
        return 50;
    }
    
}
