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
import java.util.*;
import java.util.logging.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.filter.*;
import org.mozillatranslator.kernel.*;

/** This action tries to auto-fill empty translations with other existent
 * translations for the same original string
 * 
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class AutoTranslate extends AbstractAction {
    private static final Logger fLogger = Logger.getLogger(AutoTranslate.class.getPackage().
            getName());
    
    /** Creates new NoopAction
     * @param text The text to appear in the menu
     */
    public AutoTranslate() {
        super(Kernel.translate("menu.edit.auto_translate.label"));
    }
    
    protected org.mozillatranslator.filter.Filter getFilter(String localeName) {
        return new AllForAutoTranslate(localeName);
    }
    
    // TODO Improve auto-translate key-bindings
    public void actionPerformed(ActionEvent evt) {
        List collectedList;
        List autoTranslatedList = new ArrayList();
        String localeName;
        ShowWhatDialog swd;
        List cols;
        
        swd = new ShowWhatDialog();
        
        if (swd.showDialog()) {
            localeName = swd.getSelectedLocale();
            cols = swd.getSelectedColumns();
            
            org.mozillatranslator.filter.Filter filter = getFilter(localeName);
            collectedList  = FilterRunner.filterDatamodel(filter);
            Collections.sort(collectedList);
            
            Translation currentTranslation;
            String translatedText = null;
            int i = 0;
            for (Iterator it = collectedList.iterator(); it.hasNext(); ) {
                Phrase phrase = (Phrase) it.next();
                currentTranslation = (Translation) phrase.getChildByName(localeName);
                
                String originalText = phrase.getText();
                Phrase autoTransl = null;
                if (originalText.length() > 1 && null == currentTranslation
                        && !phrase.isKeepOriginal()) {
                    autoTransl = findPhraseWithSameOriginalText(phrase.getText(),
                            collectedList, localeName);
                }
                if (null != autoTransl) {
                    currentTranslation = (Translation) autoTransl.getChildByName(localeName);
                    phrase.addChild(currentTranslation);
                    //phrase.setFuzzy(true);
                    translatedText = currentTranslation.getText();
                    
                    Phrase accessPhrase = phrase.getAccessConnection();
                    if (accessPhrase != null) {
                        Translation accessTranslation = (Translation) accessPhrase.
                                getChildByName(localeName);
                        String newAccesText = "";
                        if (accessTranslation != null) {
                            newAccesText = accessTranslation.getText();
                        } else {
                            newAccesText = currentTranslation.getText().substring(0, 1);
                        }
                        Translation newAccessTranslation = new Translation(localeName,
                                phrase, newAccesText, Translation.STATUS_TRANSLATED);
                        accessPhrase.addChild(newAccessTranslation);
                    }
                    fLogger.info("" + (i++) + "\tphrase: " + phrase.getText() +
                            ", \ttranslation: " + translatedText);
                    autoTranslatedList.add(phrase);
                }
            }
        }
    }
    
    /**
     * @return the first phrase with the same original text
     */
    private Phrase findPhraseWithSameOriginalText(String text, List collectedList,
            String localeName) {
        Phrase foundPhrase = null;
        Phrase phrase = null;
        for (Iterator it = collectedList.iterator(); it.hasNext(); ) {
            phrase = (Phrase) it.next();
            if (phrase.getText().equals(text)) {
                if (null != (Translation) phrase.getChildByName(localeName)) {
                    foundPhrase = phrase;
                    break;
                }
            }
        }
        return foundPhrase;
    }
}
