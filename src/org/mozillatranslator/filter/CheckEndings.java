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
 * Portions created by Ricardo Palomares are
 * Copyright (C) Ricardo Palomares.
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
 * Checks if a original and translated strings have the same ending
 * @author rpalomares
 */
public class CheckEndings implements Filter {
    private String localeName;
    private String endingCheckedChars;
    
    /**
     * Constructor for CheckEndings
     * @param ln Locale code
     */
    public CheckEndings(String ln) {
        localeName = ln;
        endingCheckedChars = Kernel.settings.getString(Settings.QA_ENDING_CHECKED_CHARS);
    }
    
    @Override
    public boolean check(Phrase ph) {
        boolean result;
        char endingOriginalChar;

        // We first check if all pre-requisites are satissfied
        result = (ph.getText().length() > 0);
        result = result && (!ph.isKeepOriginal());
        result = result && (ph.hasChildren());
        result = result && (ph.getChildByName(localeName) != null);
        result = result && (((Translation) ph.getChildByName(localeName)).getText() != null);
        result = result && (((Translation) ph.getChildByName(localeName)).getText().length() > 0);

        if (result) {
            endingOriginalChar = ph.getText().charAt(ph.getText().length() - 1);

            if (endingCheckedChars.indexOf(endingOriginalChar) != -1) {
                char endingTranslatedChar;
                Translation t = (Translation) ph.getChildByName(localeName);
                String translatedText = t.getText();
                endingTranslatedChar = translatedText.charAt(translatedText.length() - 1);
                result = (endingOriginalChar != endingTranslatedChar);

                if (result) {
                    ph.addFilterResult("Original and translation have different "
                            + "ending chars ([" + endingOriginalChar + "] vs. ["
                            + endingTranslatedChar + "])");
                }
            } else {
                result = false;
            }
        }
        
        return result;
    }
}
