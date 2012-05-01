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
import org.mozillatranslator.datamodel.BinaryFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.datamodel.TrnsStatus;

/**
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class CurrentTextColumn implements ComplexColumn {
    private static final Class STR_CLASS = "dummy".getClass();

    /** Creates new TranslatedTextColumn */
    public CurrentTextColumn() {
    }

    @Override
    public Class getColumnClass() {
        return STR_CLASS;
    }

    @Override
    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        String result;
        if (currentPhrase.getName().equals("MT_UknownFileType")) {
            if (currentPhrase.getParent() instanceof BinaryFile) {
                result = "Binary files cannot be translated";
            } else {
                result = "Use the edit dialog";
            }
        } else {
            Translation currentTranslation = (Translation)
                    currentPhrase.getChildByName(currentLocalization);
            if (currentTranslation != null) {
                result = currentTranslation.getText();
            } else {
                result = currentPhrase.getText();
            }
        }
        return result;
    }

    @Override
    public boolean isCellEditable(Phrase currentPhrase,
            String currentLocalization) {
        boolean result;
        if (currentPhrase.getName().equals("MT_UknownFileType")) {
            result = false;
        } else {
            result = !(currentPhrase.isKeepOriginal());
        }
        return result;
    }

    @Override
    public String getColumnName() {
        return "Text: Current";
    }

    @Override
    public void setValue(Phrase currentPhrase, Object value,
            String currentLocalization) {
        Translation currentTranslation;
        String strValue = (String) value;

        if (!strValue.equals("")) {
            currentTranslation = (Translation)
                    currentPhrase.getChildByName(currentLocalization);
            if (currentTranslation == null) {
                currentTranslation = new Translation(currentLocalization,
                        currentPhrase, strValue, TrnsStatus.Translated);
                currentPhrase.addChild(currentTranslation);
            } else {
                currentTranslation.setText(strValue);
                currentTranslation.setStatus(TrnsStatus.Translated);
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
