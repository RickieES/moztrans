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

import javax.swing.JTable;
import org.mozillatranslator.datamodel.Phrase;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class ProductColumn implements ComplexColumn {
    private static final Class<? extends Object> STR_CLASS = String.class;
    // Next line is commented out because parenList seems not being used anywhere 
    // private static final String[] parentList = {"", "", "", "", "", "", "", "", "", ""};

    /** Creates new KeyColumn */
    public ProductColumn() {
        // Nothing to do
    }
    
    @Override
    public Class<? extends Object> getColumnClass() {
        return STR_CLASS;
    }
    
    @Override
    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        return currentPhrase.getProductParent().toString();
    }
    
    @Override
    public boolean isCellEditable(Phrase currentPhrase, String currentLocalization) {
        return false;
    }
    
    @Override
    public String getColumnName() {
        return "Product";
    }
    
    @Override
    public void setValue(Phrase currentPhrase, Object value, String currentLocalization) {
        // Product column is not editable
    }
    
    @Override
    public String toString() {
        return "Product";
    }
    
    @Override
    public void init(JTable table) {
        // Nothing to do
    }
    
    @Override
    public int getPreferredWidth() {
        return 50;
    }
}
