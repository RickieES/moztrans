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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import org.mozillatranslator.datamodel.AutoAccessKeyAssign.AccessKeyBundleList;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.gui.MainWindow;
import org.mozillatranslator.gui.MozFrame;
import org.mozillatranslator.kernel.Kernel;


/** This action sets/unsets the Fuzzy or Keep Original flags
 * on every selected phrase
 *
 * @author Ricardo Palomares
 * @version 1.0
 */
public class RowBatchAction extends AbstractAction {
    public static final int FLD_FUZZY_ON   = 1;
    public static final int FLD_FUZZY_OFF  = 2;
    public static final int FLD_KEEP_ON    = 3;
    public static final int FLD_KEEP_OFF   = 4;
    public static final int FLD_TRNS_CLEAR = 5;

    private int batchType;

    /**
     * Constructor for RowBatchAction (an Action operating over a selection
     * of rows in a JTable
     * @param batchType the type of action to perform
     */
    public RowBatchAction(int batchType) {
        super();
        this.batchType = batchType;

        switch (batchType) {
            case FLD_FUZZY_ON:
                putValue(NAME, Kernel.translate("SetFuzzyOn.button"));
                putValue(Action.LARGE_ICON_KEY,
                        new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/fuzzy-on.png")));
                putValue(Action.SHORT_DESCRIPTION,
                        Kernel.translate("SetFuzzyOn.button"));
                // Accelerator keys seem not to be honored in the UI
                // putValue(ACCELERATOR_KEY, Kernel.translate("SetFuzzyOn.accesskey"));
                break;
            case FLD_FUZZY_OFF:
                putValue(NAME, Kernel.translate("SetFuzzyOff.button"));
                putValue(Action.LARGE_ICON_KEY,
                        new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/fuzzy-off.png")));
                putValue(Action.SHORT_DESCRIPTION,
                        Kernel.translate("SetFuzzyOff.button"));
                // putValue(ACCELERATOR_KEY, Kernel.translate("SetFuzzyOff.accesskey"));
                break;
            case FLD_KEEP_ON:
                putValue(NAME, Kernel.translate("SetKeepOn.button"));
                putValue(Action.LARGE_ICON_KEY,
                        new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/keep-orig.png")));
                putValue(Action.SHORT_DESCRIPTION,
                        Kernel.translate("SetKeepOn.button"));
                // putValue(ACCELERATOR_KEY, Kernel.translate("SetKeepOn.accesskey"));
                break;
            case FLD_KEEP_OFF:
                putValue(NAME, Kernel.translate("SetKeepOff.button"));
                putValue(Action.LARGE_ICON_KEY,
                        new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/unkeep-orig.png")));
                putValue(Action.SHORT_DESCRIPTION,
                        Kernel.translate("SetKeepOff.button"));
                // putValue(ACCELERATOR_KEY, Kernel.translate("SetKeepOff.accesskey"));
                break;
            case FLD_TRNS_CLEAR:
                putValue(NAME, Kernel.translate("ClearTranslation.button"));
                putValue(Action.LARGE_ICON_KEY,
                        new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/edit-clear.png")));
                putValue(Action.SHORT_DESCRIPTION,
                        Kernel.translate("ClearTranslation.button"));
                // putValue(ACCELERATOR_KEY, Kernel.translate("ClearTranslation.accesskey"));
                break;
        }
    }

    /**
     * This gets called when the event happens
     * @param evt the event
     */
    @Override public void actionPerformed(ActionEvent evt) {
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
                    tn = (Translation) ph.getChildByName(locale);

                    switch (this.batchType) {
                        case FLD_FUZZY_OFF:
                            ph.setFuzzy(false);
                            break;
                        case FLD_FUZZY_ON:
                            ph.setFuzzy(true);
                            break;
                        case FLD_KEEP_OFF:
                            ph.setKeepOriginal(false);
                            break;
                        case FLD_KEEP_ON:
                            ph.setKeepOriginal(true);
                            break;
                        case FLD_TRNS_CLEAR:
                            if (tn!=null) {
                                tn.setText("");
                            }
                            break;
                    }
                }
                table.repaint();
            }
        }
    }
}
