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
public class TranslatedStatusColumn implements ComplexColumn {
    private static final Class STATUS_CLASS = TrnsStatus.class;

    /** Creates new KeyColumn */
    public TranslatedStatusColumn() {
        // Do nothing
    }

    @Override
    public Class getColumnClass() {
        return STATUS_CLASS;
    }

    @Override
    public Object getValue(Phrase currentPhrase, String currentLocalization) {
        TrnsStatus result = null;
        Translation currentTranslation = (Translation)
                currentPhrase.getChildByName(currentLocalization);
        if (currentTranslation != null) {
            result = currentTranslation.getStatus();
        }
        return result;
    }

    @Override
    public boolean isCellEditable(Phrase currentPhrase,
            String currentLocalization) {
        boolean result = false;
        Translation currentTranslation = (Translation)
                currentPhrase.getChildByName(currentLocalization);
        if (currentTranslation != null) {
            result = true;
        }
        return result;
    }

    @Override
    public String getColumnName() {
        return "Status";
    }

    @Override
    public void setValue(Phrase currentPhrase, Object value,
            String currentLocalization) {
        Translation currentTranslation = (Translation)
                currentPhrase.getChildByName(currentLocalization);
        if (currentTranslation != null) {
            currentTranslation.setStatus((TrnsStatus) value);
        }
    }

    @Override
    public String toString() {
        return "Status";
    }

    @Override
    public void init(JTable table) {
        JComboBox editCombo = new JComboBox();

        for(TrnsStatus ts : TrnsStatus.values()) {
            editCombo.addItem(ts);
        }
        table.setDefaultEditor(TrnsStatus.class, new DefaultCellEditor(editCombo));
    }

    @Override
    public int getPrefferedWidth() {
        return 50;
    }
}
