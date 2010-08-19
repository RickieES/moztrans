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
package org.mozillatranslator.action;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;

/** This action opens the Edit Phrase Dialog
 * on the currently selected phrase.
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class EditPhraseAction extends AbstractAction {
    /** Creates new EditPhraseAction */
    public EditPhraseAction() {
        super(Kernel.translate("menu.edit.edit_selected_phrase.label"));
        putValue(Action.LARGE_ICON_KEY,
                 new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/edit-phrase.png")));
        putValue(Action.SHORT_DESCRIPTION,
                Kernel.translate("menu.edit.edit_selected_phrase.label"));
    }

    /** this gets called when the event happens
     * @param evt the event
     */
    @Override public void actionPerformed(ActionEvent evt) {
        MainWindow mw;
        MozFrame mf;
        Phrase ph;
        String name;
        JTable table;
        int selectedIndex;
        int maxIndex;

        mw = Kernel.mainWindow;
        mf = mw.getInnerFrame();
        if (mf != null) {
            ph = mf.getSelectedPhrase();
            if (ph != null) {
                selectedIndex = mf.getSelectionIndex();
                maxIndex = mf.getMaxIndex();
                name = mf.getLocalization();
                table = mf.getTable();
                table.editingCanceled(new ChangeEvent(this));
                Kernel.editPhrase.showDialog(ph, name, selectedIndex, maxIndex, mf);
            }
        }
    }
}
