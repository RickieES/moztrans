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
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.datamodel.TrnsStatus;

/**
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class TranslatedCommandColumn implements ComplexColumn {
    private static final Class STR_CLASS = "dummy".getClass();

    /** Creates new OriginalTextColumn */
    public TranslatedCommandColumn() {
    }

    @Override
    public Class getColumnClass() {
        return STR_CLASS;
    }

    @Override
    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        String result = "";
        Phrase commandPhrase = currentPhrase.getCommandConnection();
        if (commandPhrase != null) {
            Translation commandTranslation = (Translation)
                    commandPhrase.getChildByName(currentLocalization);
            if (commandTranslation != null) {
                result = commandTranslation.getText();
            }
        }
        return result;
    }

    @Override
    public boolean isCellEditable(Phrase currentPhrase,
            String currentLocalization) {
        boolean result = false;
        Phrase commandPhrase = currentPhrase.getCommandConnection();
        if (commandPhrase != null) {
            result = true;
        }
        return result;
    }

    @Override
    public String getColumnName() {
        return "Commandkey: Translated";
    }

    @Override
    public void setValue(Phrase currentPhrase, Object value,
            String currentLocalization) {
        String strValue = (String) value;
        Phrase commandPhrase = currentPhrase.getCommandConnection();

        if (!strValue.equals("")) {
            Translation commandTranslation = (Translation)
                    commandPhrase.getChildByName(currentLocalization);
            if (commandTranslation == null) {
                commandTranslation = new Translation(currentLocalization,
                        currentPhrase, strValue, TrnsStatus.Translated);
                commandPhrase.addChild(commandTranslation);
            } else {
                commandTranslation.setText(strValue);
                commandTranslation.setStatus(TrnsStatus.Translated);
            }
        } else {
            currentPhrase.removeChild(currentPhrase.getChildByName(currentLocalization));
        }
    }

    @Override
    public String toString() {
        return getColumnName();
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
