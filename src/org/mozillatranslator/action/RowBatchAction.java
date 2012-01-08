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
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.datamodel.TrnsStatus;
import org.mozillatranslator.gui.MainWindow;
import org.mozillatranslator.gui.MozFrame;
import org.mozillatranslator.gui.dialog.MassChangePanel;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.util.GuiTools;

/** This action sets/unsets the Fuzzy or Keep Original flags
 * on every selected phrase
 *
 * @author Ricardo Palomares
 * @version 1.0
 */
public class RowBatchAction extends AbstractAction {
    private FieldToChange batchType;
    private List<ChangeUnit> cul;

    /**
     * Constructor for RowBatchAction (an Action operating over a selection
     * of rows in a JTable)
     * @param batchType the type of action to perform
     */
    public RowBatchAction(FieldToChange batchType, String value) {
        super();
        String buttonLabel, imagePathfile, buttonAccesskey;

        cul = new ArrayList<ChangeUnit>();
        cul.add(new ChangeUnit(batchType, value));
        this.batchType = batchType;

        switch (batchType) {
            case FUZZY:
                if (value.equals("true")) {
                    buttonLabel = "SetFuzzyOn.button";
                    imagePathfile = "/org/mozillatranslator/resource/fuzzy-on.png";
                    buttonAccesskey = "SetFuzzyOn.accesskey";
                } else {
                    buttonLabel = "SetFuzzyOff.button";
                    imagePathfile = "/org/mozillatranslator/resource/fuzzy-off.png";
                    buttonAccesskey = "SetFuzzyOff.accesskey";
                }
                break;
            case KEEPORIG:
                if (value.equals("true")) {
                    buttonLabel = "SetKeepOn.button";
                    imagePathfile = "/org/mozillatranslator/resource/keep-orig.png";
                    buttonAccesskey = "SetKeepOn.accesskey";
                } else {
                    buttonLabel = "SetKeepOff.button";
                    imagePathfile = "/org/mozillatranslator/resource/unkeep-orig.png";
                    buttonAccesskey = "SetKeepOff.accesskey";
                }
                break;
            case TRANSLATION:
                buttonLabel = "ClearTranslation.button";
                imagePathfile = "/org/mozillatranslator/resource/edit-clear.png";
                buttonAccesskey = "ClearTranslation.accesskey";
                break;
            case MULTIPLE:
            default:
                buttonLabel = "MassChange.button";
                imagePathfile = "/org/mozillatranslator/resource/mass-change.png";
                buttonAccesskey = "MassChange.accesskey";
                break;
        }

        putValue(NAME, Kernel.translate(buttonLabel));
        putValue(Action.LARGE_ICON_KEY,
                new ImageIcon(getClass().getResource(imagePathfile)));
        putValue(Action.SHORT_DESCRIPTION, Kernel.translate(buttonLabel));
        // putValue(ACCELERATOR_KEY, Kernel.translate(buttonAccesskey));
    }

    /**
     * This gets called when the event happens
     * @param evt the event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        MainWindow mw;
        MozFrame mf;
        ArrayList<Phrase> phraseList;
        Translation tn;
        String locale;
        JTable table;
        MassChangePanel mcPanel = new MassChangePanel();

        if (cul.get(0).getChangeType() == FieldToChange.MULTIPLE) {
            JDialog mcDialog = new JDialog(Kernel.mainWindow,
                    Kernel.translate("dialog.masschange.title"), true);
            mcDialog.setContentPane(mcPanel);
            mcDialog.pack();
            GuiTools.placeFrameAtCenter(mcDialog);
            mcDialog.setVisible(true);

            if (mcPanel.isOkPressed()) {
                cul = mcPanel.getChangesToDo();
            } else {
                return;
            }
        }

        mw = Kernel.mainWindow;
        mf = mw.getInnerFrame();
        if (mf != null) {
            phraseList = mf.getSelectedPhrases();
            if (phraseList != null) {
                locale = mf.getLocalization();
                table = mf.getTable();
                table.editingCanceled(new ChangeEvent(this));

                for(Phrase ph : phraseList) {
                    tn = (Translation) ph.getChildByName(locale);

                    for(ChangeUnit cu : cul) {
                        switch (cu.getChangeType()) {
                            case FUZZY:
                                ph.setFuzzy((cu.getChangeValue().equals("true")));
                                break;
                            case KEEPORIG:
                                ph.setKeepOriginal((cu.getChangeValue().equals("true")));
                                break;
                            case TRANSLATION:
                                if (tn != null) {
                                    tn.setText(cu.getChangeValue());
                                }
                                break;
                            case TRNSSTATUS:
                                if (tn != null) {
                                    tn.setStatus(TrnsStatus.valueOf(cu.getChangeValue()));
                                }
                                break;
                        }
                    }
                }
                table.repaint();
            }
        }

        if (this.batchType == FieldToChange.MULTIPLE) {
            cul.clear();
            cul.add(new ChangeUnit(this.batchType, ""));
        }
    }
}
