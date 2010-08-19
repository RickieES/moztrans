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

package org.mozillatranslator.datamodel;

import org.mozillatranslator.kernel.EmptyTraverseCommand;
import org.mozillatranslator.kernel.TraverseCommand;

/** This class gathers statistics about a file, component or product
 *
 * @author rpalomares
 */
public class StatisticsTraverse extends EmptyTraverseCommand
        implements TraverseCommand {
    private String l10n;
    private int totalStrings = 0;
    private int translatedStrings = 0;
    private int keepOrigStrings = 0;
    private int untranslatedStrings = 0;
    private int fuzzyStrings = 0;
    
    /** Creates a new instance of StatisticsTraverse */
    public StatisticsTraverse(String l10n) {
        this.l10n = l10n;
    }
    
    public boolean action(Phrase currentPhrase) {
        Translation curTrans;
        
        totalStrings++;
        
        curTrans = (Translation) currentPhrase.getChildByName(l10n);
        
        // If there is a translation
        if (curTrans != null) {
            // We count it only if KeepOriginal is not set
            translatedStrings += (currentPhrase.isKeepOriginal()) ? 0 : 1;
        }
        
        // We count KeepOriginal strings whether translation exists or not
        keepOrigStrings += (currentPhrase.isKeepOriginal()) ? 1 : 0;
        
        // Fuzzy strings count is independent of the other values
        fuzzyStrings += (currentPhrase.isFuzzy()) ? 1 : 0;
        
        return true;
    }
    
    public void calculateUntranslated() {
        untranslatedStrings = totalStrings - translatedStrings - keepOrigStrings;
    }

    public int getTotalStrings() {
        return totalStrings;
    }

    public int getTranslatedStrings() {
        return translatedStrings;
    }

    public int getKeepOrigStrings() {
        return keepOrigStrings;
    }

    public int getUntranslatedStrings() {
        return untranslatedStrings;
    }

    public int getFuzzyStrings() {
        return fuzzyStrings;
    }
}
