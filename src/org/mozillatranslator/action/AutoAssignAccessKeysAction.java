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

package org.mozillatranslator.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import org.mozillatranslator.datamodel.AutoAccessKeyAssign.AccessKeyBundle;
import org.mozillatranslator.datamodel.AutoAccessKeyAssign.AccessKeyBundleList;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.gui.MainWindow;
import org.mozillatranslator.gui.MozFrame;
import org.mozillatranslator.gui.dialog.AutoAccessKeyDialog;
import org.mozillatranslator.kernel.Kernel;

/**
 * This action grabs all selected rows in a JTable and opens the
 * Auto Assign Accesskeys dialog.
 * 
 * @author rpalomares
 */
public class AutoAssignAccessKeysAction extends AbstractAction {
    
    /**
     * Constructor for AutoAssignAccessKeysAction
     */
    public AutoAssignAccessKeysAction() {
        super();
        putValue(NAME, Kernel.translate("AutoAssignAccesskeys.button"));
        putValue(Action.LARGE_ICON_KEY,
                 new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/autoassign-ak.png")));
        putValue(Action.SHORT_DESCRIPTION,
                Kernel.translate("AutoAssignAccesskeys.button"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainWindow mw;
        MozFrame mf;
        ArrayList<Phrase> phraseList;
        Iterator<Phrase> it;
        Phrase ph;
        Translation tn;
        String locale;
        JTable table;
        AccessKeyBundleList akl;
        
        mw = Kernel.mainWindow;
        mf = mw.getInnerFrame();
        if (mf != null) {
            phraseList = mf.getSelectedPhrases();
            if (phraseList != null) {
                locale = mf.getLocalization();
                it = phraseList.iterator();
                table = mf.getTable();
                table.editingCanceled(new ChangeEvent(this));
                akl = new AccessKeyBundleList(locale);
                
                while (it.hasNext()) {
                    ph = it.next();
                    akl.add(new AccessKeyBundle(ph, locale));
                }
                akl.cleanList();
                AutoAccessKeyDialog akd = new AutoAccessKeyDialog(akl);
                
                // Since akd is modal, next line will stop execution in this
                // method until the accesskey auto assignment dialog is closed
                akd.setVisible(true);
                table.repaint();
            }
        }
    }
}
