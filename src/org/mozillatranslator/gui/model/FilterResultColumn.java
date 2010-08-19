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
 * Ricardo Palomares (Initial Code)
 *
 */

package org.mozillatranslator.gui.model;

import javax.swing.JTable;
import org.mozillatranslator.datamodel.Phrase;


/** This class displays aggregated results from filters
 *  (org.mozillatranslator.filter) and other methods
 *
 * @author  Ricardo Palomares
 * @version 1.0
 */
public class FilterResultColumn implements ComplexColumn {
    private static final Class STR_CLASS = "dummy".getClass();

    public void init(JTable table) {
    }

    public String getColumnName() {
        return "Result comment";
    }
    
    public String toString() {
        return getColumnName();
    }

    public Class getColumnClass() {
        return STR_CLASS;
    }

    public boolean isCellEditable(Phrase currentPhrase, String currentLocalization) {
        return false;
    }

    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        return currentPhrase.getFilterResult();
    }

    public void setValue(Phrase currentPhrase, Object value, String currentLocalization) {
        // This column doesn't allow editing
    }

    public int getPrefferedWidth() {
        return 200;
    }
}
