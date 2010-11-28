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
    private static final Class STATUS_CLASS = (new StatusCell(-1, "")).getClass();

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
        StatusCell result = StatusCell.getNoStatusCell();
        Translation currentTranslation = (Translation)
                currentPhrase.getChildByName(currentLocalization);
        if (currentTranslation != null) {
            result = StatusCell.lookupStatusCell(currentTranslation.getStatus());
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
        StatusCell cellValue = (StatusCell) value;
        Translation currentTranslation = (Translation)
                currentPhrase.getChildByName(currentLocalization);
        if (currentTranslation != null) {
            currentTranslation.setStatus(cellValue.getKey());
        }
    }

    @Override
    public String toString() {
        return "Status";
    }

    @Override
    public void init(JTable table) {
        //do stuff
    /*
        public static final int STATUS_NOTSEEN=0;
        public static final int STATUS_CHANGED    = 1;
        public static final int STATUS_TRANSLATED = 2;
        public static final int STATUS_ERROR=3;
        public static final int STATUS_ACCEPTED=4;
        public static final int STATUS_PERFECT=5;
        public static final int STATUS_OTHER=6;
        public static final int STATUS_MIGRATED=7;
         */
        JComboBox editCombo = new JComboBox();

        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_NOTSEEN));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_CHANGED));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_TRANSLATED));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_ERROR));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_ACCEPTED));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_PERFECT));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_OTHER));
        editCombo.addItem(StatusCell.lookupStatusCell(Translation.STATUS_MIGRATED));
        table.setDefaultEditor(StatusCell.class, new DefaultCellEditor(editCombo));
    }

    @Override
    public int getPrefferedWidth() {
        return 50;
    }
}
