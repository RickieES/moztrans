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
public class CurrentAccessColumn implements ComplexColumn {

    private static final Class STR_CLASS = "dummy".getClass();

    /** Creates new OriginalTextColumn */
    public CurrentAccessColumn() {
    }

    @Override
    public Class getColumnClass() {
        return STR_CLASS;
    }

    @Override
    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        String result = "";
        Phrase accessPhrase = currentPhrase.getAccessConnection();
        if (accessPhrase != null) {
            Translation accessTranslation = (Translation)
                    accessPhrase.getChildByName(currentLocalization);
            if (accessTranslation != null) {
                result = accessTranslation.getText();
            } else {
                result = accessPhrase.getText();
            }
        }
        return result;
    }

    @Override
    public boolean isCellEditable(Phrase currentPhrase,
                                  String currentLocalization) {
        boolean result = false;
        Phrase accessPhrase = currentPhrase.getAccessConnection();
        if (accessPhrase != null) {
            result = true;
        }
        return result;
    }

    @Override
    public String getColumnName() {
        return "Accesskey: Current";
    }

    @Override
    public void setValue(Phrase currentPhrase, Object value,
                         String currentLocalization) {
        String strValue = (String) value;
        Phrase accessPhrase = currentPhrase.getAccessConnection();

        if (!strValue.equals("")) {
            Translation accessTranslation = (Translation)
                    accessPhrase.getChildByName(currentLocalization);
            if (accessTranslation == null) {
                accessTranslation = new Translation(currentLocalization,
                        currentPhrase, strValue, TrnsStatus.Translated);
                accessPhrase.addChild(accessTranslation);
            } else {
                accessTranslation.setText(strValue);
                accessTranslation.setStatus(TrnsStatus.Translated);
            }
        } else {
            currentPhrase.removeChild(currentPhrase.getChildByName(
                    currentLocalization));
        }
    }

    @Override
    public String toString() {
        return "Accesskey: Current";
    }

    @Override
    public void init(JTable table) {
        // Nothing to do
    }

    @Override
    public int getPrefferedWidth() {
        return 50;
    }
}
