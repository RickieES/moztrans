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
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.datamodel.TrnsStatus;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.kernel.*;

/** This action gets the selected internal frame, then the selected row in the
 *  JTable contained in it, and then copies the translation used for that row
 *  in every other string in the JTable with the same original text
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class CopyToAllInRedundantView extends AbstractAction {
    /** Creates new CopyToAllInRedundantView
     */
    public CopyToAllInRedundantView() {
        super(Kernel.translate("button.copy_to_all.label"), null);
        putValue(Action.LARGE_ICON_KEY,
                 new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/edit-copytoall.png")));
        putValue(Action.SHORT_DESCRIPTION,
                Kernel.translate("button.copy_to_all.label"));
    }
    
    /** Called when the action is triggered
     * @param evt The action event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        MainWindow mw = Kernel.mainWindow;
        MozFrame mf = mw.getInnerFrame();
        String l10n;
        Phrase source, dest;
        Translation sourceTranslation, destTranslation;
        String sourceOrigText;
        String sourceTransText;
        int firstSelectedIndex = 0;
        int curSelectedIndex;
        boolean done;
        
        if (mf != null) {
            l10n = mf.getLocalization();
            source = mf.getSelectedPhrase();
            
            if (source != null) {
                // find sources to copy
                sourceOrigText = source.getText();
                sourceTranslation = (Translation) source.getChildByName(l10n);
                
                if (sourceTranslation != null) {
                    sourceTransText = sourceTranslation.getText();
                } else {
                    sourceTransText = null;
                }
                // find the first with that text
                curSelectedIndex = mf.getSelectionIndex();
                done = false;
                while (!done) {
                    if (curSelectedIndex == 0) {
                        firstSelectedIndex = 0;
                        done = true;
                    } else {
                        curSelectedIndex--;
                        
                        Phrase testPhrase = mf.getPhraseByIndex(curSelectedIndex);
                        
                        if (!testPhrase.getText().equals(sourceOrigText)) {
                            done = true;
                            firstSelectedIndex = curSelectedIndex + 1;
                        }
                    }
                }
                
                done = false;
                curSelectedIndex = firstSelectedIndex;
                while (!done) {
                    dest = mf.getPhraseByIndex(curSelectedIndex);
                    if (dest.getText().equals(sourceOrigText)) {
                        if (sourceTransText == null) {
                            destTranslation = (Translation) dest.getChildByName(l10n);
                            
                            if (destTranslation != null) {
                                dest.removeChild(destTranslation);
                            }
                        } else {
                            destTranslation = (Translation) dest.getChildByName(l10n);
                            
                            if (destTranslation != null) {
                                destTranslation.setText(sourceTransText);
                                destTranslation.setStatus(TrnsStatus.Copied);
                            } else {
                                destTranslation = new Translation(l10n, dest,
                                        sourceTransText, TrnsStatus.Copied);
                                dest.addChild(destTranslation);
                            }
                        }
                    } else {
                        done = true;
                    }
                    curSelectedIndex++;
                }
                mf.tableRowsChanged(firstSelectedIndex, curSelectedIndex);
            }
        }
    }
}
