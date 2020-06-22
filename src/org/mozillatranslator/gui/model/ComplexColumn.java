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
 * Ricardo Palomares (Documentation of interface methods)
 */

package org.mozillatranslator.gui.model;

import javax.swing.JTable;
import org.mozillatranslator.datamodel.Phrase;

/**
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public interface ComplexColumn {
    /**
     * Hook for columns that need initialization before being added to a JTable
     * @param table the table to which the column will be added
     */
    public void init(JTable table);

    /**
     * Column header text
     * @return the text to be displayed as column header
     */
    public String getColumnName();

    /**
     * Returns the class type of the column contents
     * @return the class type of the column contents
     */
    public Class<? extends Object> getColumnClass();

    /**
     * Reports if a specific cell of this column is editable
     * @param currentPhrase the current Phrase of the row
     * @param currentLocalization the locale code (ie., the child of the Phrase)
     * @return true if the cell can be edited
     */
    public boolean isCellEditable(Phrase currentPhrase,
            String currentLocalization);

    /**
     * Returns the current value of the cell for the column and row
     * @param currentPhrase the Phrase object of the current row
     * @param currentLocalization the child of the Phrase
     * @return the value of the cell
     */
    public Object getValue(Phrase currentPhrase, String currentLocalization);

    /**
     * Sets the current value of the cell for the column and row
     * @param currentPhrase the Phrase object of the current row
     * @param value the value to be set
     * @param currentLocalization the child of the Phrase
     */
    public void setValue(Phrase currentPhrase, Object value,
            String currentLocalization);

    /**
     * Returns the preferred width for the column
     * @return the preferred width for the column
     */
    public int getPreferredWidth();
}
