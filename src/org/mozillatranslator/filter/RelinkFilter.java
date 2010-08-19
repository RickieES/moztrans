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

import org.mozillatranslator.datamodel.*;

/**
 *
 * @author rpalomares
 */
public class RelinkFilter implements Filter {
    private String localeName;
    private boolean keepExisting;
    
    /**
     * Creates a new instance of a relink filter, which provides the needed tests
     * to relinks between labels, accesskeys and commandkeys
     *
     * @param ln        the locale name
     * @param keep      true if existing links mut be preserved
     */
    public RelinkFilter(String ln, boolean keep) {
        this.localeName = ln;
        this.keepExisting = keep;
    }
    
    /**
     * This will return true if the phrase qualifies as a label and its connections
     * to accesskeys and commandkeys have been modified
     *
     * @param phrase    the Phrase object to be "filtered"
     * @return true     if the phrase has had its connections relinked
     */
    public boolean check(Phrase phrase) {
        boolean result = false;
        String currentAKey = phrase.getAccessConnection().toString();
        String currentCKey = phrase.getCommandConnection().toString();
        
        if (phrase.isLabel()) {
            result = phrase.linkLabel2Keys(keepExisting);
            
            if (result) {
                if (!currentAKey.equals(phrase.getAccessConnection().toString())) {
                    phrase.addFilterResult("Accesskey changed from [" +
                            currentAKey + "] to [" +
                            phrase.getAccessConnection().toString());
                }
                
                if (!currentCKey.equals(phrase.getCommandConnection().toString())) {
                    phrase.addFilterResult("Commandkey changed from [" +
                            currentCKey + "] to [" +
                            phrase.getCommandConnection().toString());
                }
            }
        }
        
        return result;
    }

    public String getLocaleName() {
        return localeName;
    }
    
}
