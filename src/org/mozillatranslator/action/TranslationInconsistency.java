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
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.filter.*;
import org.mozillatranslator.kernel.*;

/** Looks for strings with the same original and different translations
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class TranslationInconsistency extends AbstractAction {
    /** Creates new NoopAction
     */
    public TranslationInconsistency() {
        super(Kernel.translate("menu.edit.qa.translation_inconsistency.label"));
    }
    
    /**
     * Returns the All for auto-translate filter
     * @param localeName
     * @return the All for auto-translate filter
     */
    protected Filter getFilter(String localeName) {
        return new AllForAutoTranslate(localeName);
    }
    
    // TODO Improve auto-translate key-bindings
    @Override
    public void actionPerformed(ActionEvent evt) {
        List collectedList;
        List translationInconsistencyList = new ArrayList();
        String localeName;
        ShowWhatDialog swd;
        List cols;
        
        swd = new ShowWhatDialog();
        
        if (swd.showDialog()) {
            localeName = swd.getSelectedLocale();
            cols = swd.getSelectedColumns();
            Filter filter = getFilter(localeName);
            collectedList  = FilterRunner.filterDatamodel(filter);
            Collections.sort(collectedList);
            Translation currentTranslation;
            String translatedText = null;
            int i = 0;
            for (Iterator it = collectedList.iterator(); it.hasNext(); ) {
                Phrase phrase = (Phrase) it.next();
                currentTranslation = (Translation) phrase.getChildByName(localeName);
                
                String originalText = phrase.getText();
                List autoTransl = null;
                if (originalText.length() > 1 && null != currentTranslation
                        && !phrase.isKeepOriginal()) {
                    autoTransl = findPhraseWithDifferentTranslation(phrase, collectedList, localeName);
                }
                if (null != autoTransl) {
                    /*
                    currentTranslation = (Translation) autoTransl.getChildByName(localeName);
                    phrase.addChild(currentTranslation);
                    //phrase.setFuzzy(true);
                    translatedText = currentTranslation.getText();
                    
                    Phrase accessPhrase = phrase.getAccessConnection();
                    if (accessPhrase != null) {
                        Translation accessTranslation = (Translation) accessPhrase.getChildByName(localeName);
                        String newAccesText = "";
                        if (accessTranslation != null) {
                            newAccesText = accessTranslation.getText();
                        } else {
                            newAccesText = currentTranslation.getText().substring(0, 1);
                        }
                        Translation newAccessTranslation = new Translation(localeName, phrase,
                                newAccesText, Translation.STATUS_TRANSLATED);
                        accessPhrase.addChild(newAccessTranslation);
                    }
                    System.out.println("" + (i++) + "\tphrase: " + phrase.getText()
                    + ", \ttranslation: " + translatedText);
                    */
                    if (autoTransl.size() > 0) {
                        for (Iterator itFound = autoTransl.iterator(); itFound.hasNext(); ) {
                            translationInconsistencyList.add(itFound.next());
                        }
                        translationInconsistencyList.add(phrase);
                    }
                }
            }
            new ComplexTableWindow("Translation inconsistency",
                    translationInconsistencyList, cols, localeName, null);
        }
    }
    
    /**
     * @return
     */
    private List findPhraseWithDifferentTranslation(Phrase currentPhrase,
            List collectedList, String localeName) {
        Translation currentTranslation = (Translation) currentPhrase.getChildByName(localeName);
        
        List found = new ArrayList();
        Phrase phrase = null;
        for (Iterator it = collectedList.iterator(); it.hasNext(); ) {
            phrase = (Phrase) it.next();
            if (phrase != currentPhrase && phrase.getText().equals(currentPhrase.getText())) {
                Translation transl = (Translation) phrase.getChildByName(localeName);
                if (null != transl && !currentTranslation.getText().equals(transl.getText())) {
                    found.add(phrase);
                }
            }
        }
        return found;
    }
}
