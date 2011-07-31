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
import org.mozillatranslator.datamodel.GenericFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.datamodel.TrnsStatus;


/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class TranslatedTextColumn implements ComplexColumn {
    private static final Class STR_CLASS = "dummy".getClass();

    /** Creates new TranslatedTextColumn */
    public TranslatedTextColumn() {
    }

    @Override
    public Class getColumnClass() {
        return STR_CLASS;
    }

    @Override
    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        boolean test = false;
        String result = "";
        String dlgType = "";

        if (currentPhrase.getName().equals("MT_UknownFileType")) {
            GenericFile curFile = (GenericFile) currentPhrase.getParent();

            if (curFile instanceof BinaryFile) {
                test = (((BinaryFile) curFile).getTranslatedContent() == null);
                dlgType = "Image";
            } else {
                test = (currentPhrase.getChildByName(currentLocalization) == null);
                dlgType = "Phrase";
            }

            if (test) {
                result = "No translated content available";
            } else {
                result = "Use the Edit " + dlgType + " Dialog";
            }
        } else {
            Translation currentTranslation = (Translation)
                    currentPhrase.getChildByName(currentLocalization);

            if (currentTranslation != null) {
                result = currentTranslation.getText();
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
            if (currentPhrase.isKeepOriginal()) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String getColumnName() {
        return "Text: Translated";
    }

    @Override
    public void setValue(Phrase currentPhrase, Object value,
            String currentLocalization) {
        Translation currentTranslation;
        String strValue = (String) value;

        if (!strValue.equals("")) {
            currentTranslation = (Translation) currentPhrase.getChildByName(
                    currentLocalization);

            if (currentTranslation == null) {
                currentTranslation = new Translation(currentLocalization,
                        currentPhrase, strValue, TrnsStatus.Translated);
                currentPhrase.addChild(currentTranslation);
            } else {
                currentTranslation.setText(strValue);
                currentTranslation.setStatus(TrnsStatus.Translated);
            }
        } else {
            currentPhrase.removeChild(currentPhrase.getChildByName(
                    currentLocalization));
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
        return 200;
    }
}
