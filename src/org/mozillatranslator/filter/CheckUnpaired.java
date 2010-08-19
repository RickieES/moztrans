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

import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 * Check for mismatched pairs in translations
 * @author rpalomares
 */
public class CheckUnpaired implements Filter {
    private String localeName;
    private String pairedCharList;

    /**
     * Constructor for CheckUnpaired filter
     * @param ln Locale code to check
     */
    public CheckUnpaired(String ln) {
        Settings set = Kernel.settings;
        
        pairedCharList = set.getString(Settings.QA_PAIRED_CHARS_LIST);
        if (pairedCharList.length() % 2 == 1) {
            pairedCharList = pairedCharList.substring(0, pairedCharList.length() - 1);
        }
        localeName = ln;
    }
    
    /**
     * Checks the translation of the passed-in Phrase for mismatched pairs
     * @param ph the Phrase to check
     * @return true if mismatched pairs were found
     */
    public boolean check(Phrase ph) {
        boolean result = false;
        String text;
        Translation currentTranslation;
        
        // Find the correct text
        currentTranslation = (Translation) ph.getChildByName(localeName);
        if (currentTranslation != null) {
            text = currentTranslation.getText();
            for(int i = 0; i < pairedCharList.length(); i += 2) {
                String openChar = pairedCharList.substring(i, i + 1);
                int openCount = 0;
                int openPos = 0;
                
                while (text.indexOf(openChar, openPos) != -1) {
                    openPos = text.indexOf(openChar, openPos) + 1;
                    openCount++;
                }
                
                String closeChar = pairedCharList.substring(i + 1, i + 2);
                int closeCount = 0;
                int closePos = 0;
                
                while (text.indexOf(closeChar, closePos) != -1) {
                    closePos = text.indexOf(closeChar, closePos) + 1;
                    closeCount++;
                }
                
                result = (openCount != closeCount);
                if (result) {
                    ph.addFilterResult("Mismatched pair -->" + openChar
                                       + closeChar + "<--");
                }
            }
        }
        return result;
    }
}
