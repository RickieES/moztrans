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

package org.mozillatranslator.filter;

import java.util.StringTokenizer;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;

/**
 * Checks for duplicated words in translations
 * @author rpalomares
 */
public class CheckDuplicated implements Filter {
    private String localeName;

    /**
     * Constructor for CheckDuplicated
     * @param ln Locale code to check
     */
    public CheckDuplicated(String ln) {
        localeName = ln;
    }
    
    /**
     * Checks the translation of the passed-in Phrase for duplicated words
     * @param ph the Phrase to check
     * @return true if duplicated words were found
     */
    public boolean check(Phrase ph) {
        boolean result = false;
        String text;
        Translation currentTranslation;
        
        // Find the correct text
        currentTranslation = (Translation) ph.getChildByName(localeName);
        if (currentTranslation != null) {
            text = currentTranslation.getText();
        } else {
            text = "";
        }
        
        String previous, current;
        StringTokenizer st = new StringTokenizer(text);
        previous = null;
        while ((!result) && st.hasMoreTokens()) {
            current = st.nextToken();
            result = result || (current.equals(previous));
            previous = current;
            
            if (result) {
                ph.addFilterResult("At least a duplicated word exists ["
                                   + current + "]");
            }
        }
        return result;
    }
}
