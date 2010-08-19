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
import org.mozillatranslator.util.Soundex;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.filter.*;
import org.mozillatranslator.kernel.*;

/** This is a dummy action that does nothing.
 * It is used to create the disabled menu items.
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class SearchSimilar extends AbstractAction {
    /** Creates new NoopAction
     */
    public SearchSimilar() {
        super(Kernel.translate("menu.edit.qa.search_similar.label"));
    }
    
    /**
     * Returns the the filter to apply to the query
     * @param localeName the locale code (e.g.: ab-CD)
     * @return a Filter that can be used to select Phrase objects
     */
    protected Filter getFilter(String localeName) {
        return new AllForAutoTranslate(localeName);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List collectedList;
        List similarList = new ArrayList();
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
                Phrase autoTransl = null;
                if (originalText.length() > 1 && null == currentTranslation
                        && !phrase.isKeepOriginal()) {
                    autoTransl = findPhraseWithSimilarOriginalText(phrase.getText(), collectedList, localeName);
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
                    similarList.add(autoTransl);
                    similarList.add(phrase);
                }
            }
            
            new ComplexTableWindow("Auto-translated phrases", similarList, cols,
                                   localeName, null);
        }
    }
    
    /**
     * @return
     */
    private Phrase findPhraseWithSimilarOriginalText(String text,
            List collectedList, String localeName) {
        Soundex soundex = new Soundex();
        Phrase foundPhrase = null;
        Phrase phrase = null;
        for (Iterator it = collectedList.iterator(); it.hasNext(); ) {
            phrase = (Phrase) it.next();
            String s1 = "";
            String s2 = "";
            try {
                s1= soundex.soundex(phrase.getText());
                s2 = soundex.soundex(text);
            } catch (RuntimeException e) {
            }
            if (s1.length() > 0 && s1.equals(s2)) {
                if (null != (Translation) phrase.getChildByName(localeName)) {
                    foundPhrase = phrase;
                    break;
                }
            }
        }
        return foundPhrase;
    }
}
