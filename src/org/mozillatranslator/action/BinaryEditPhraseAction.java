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
import org.mozillatranslator.gui.dialog.*;

/**
 * This action opens the Edit Phrase Dialog
 * on the currently selected phrase if it is a binary file.
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class BinaryEditPhraseAction extends AbstractAction {
    /** Creates new InstallManagerAction */
    public BinaryEditPhraseAction() {
        super(Kernel.translate("button.edit_binary_file.label"), null);
        putValue(Action.LARGE_ICON_KEY,
                 new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/edit-image.png")));
        putValue(Action.SHORT_DESCRIPTION,
                Kernel.translate("button.edit_binary_file.label"));
    }
    
    /** this gets called when the event happens
     * @param evt The event object.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        MainWindow mw;
        MozFrame mf;
        Phrase ph;
        String name;
        JTable table;
        
        mw = Kernel.mainWindow;
        mf = mw.getInnerFrame();
        
        if (mf != null) {
            ph = mf.getSelectedPhrase();
            // HACK: instanceof is a bad design, binary support must be refactored.
            if (ph != null && ph.getParent() instanceof BinaryFile) {
                BinaryFile binFile = (BinaryFile) ph.getParent();
                
                name = mf.getLocalization();
                table = mf.getTable();
                
                table.editingCanceled(new ChangeEvent(this));
                BinaryEditDialog binEdit = new BinaryEditDialog();
                binEdit.showDialog(binFile);
            }
        }
    }
}
