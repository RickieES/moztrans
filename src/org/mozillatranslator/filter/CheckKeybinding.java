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

package org.mozillatranslator.filter;

import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;

/**
 * Filter to check keybinding in translation
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class CheckKeybinding implements Filter {
    private String localeName;
    
    /** Creates new CheckKeybinding
     * @param ln Locale code to check
     */
    public CheckKeybinding(String ln) {
        localeName = ln;
    }
    
    @Override
    public boolean check(Phrase ph) {
        boolean result = false;
        Phrase accessPhrase;
        Translation currentTranslation;
        Translation accessTranslation;
        String text;
        String key;
        
        // Find the correct text
        currentTranslation = (Translation) ph.getChildByName(localeName);
        if (currentTranslation == null) {
            text = ph.getText();
        } else {
            text = currentTranslation.getText();
        }
        
        // Check for accesskey
        accessPhrase = ph.getAccessConnection();
        
        if (accessPhrase != null) {
            accessTranslation = (Translation) accessPhrase.getChildByName(localeName);
            
            if (accessTranslation == null) {
                key = accessPhrase.getText();
            } else {
                key = accessTranslation.getText();
            }
            text = text.toLowerCase();
            key  = key.toLowerCase();
            
            if (text.indexOf(key) == -1) {
                result = true;
                ph.addFilterResult("Current accesskey [" + key + "] is not present in string");
            }
        }
        return result;
    }
}
