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

/**
 *
 * @author rpalomares
 */
public class DontTranslate implements Filter {
    private String localeName;
    
    /** Creates a new instance of DontTranslate
     * @param ln Locale code
     */
    public DontTranslate(String ln) {
        this.localeName = ln;
    }

    @Override
    public boolean check(Phrase ph) {
        boolean result;
        
        result = (ph.getLocalizationNote() != null);
        result = result && (ph.getLocalizationNote().indexOf("DONT_TRANSLATE") > -1);
        result = result && (ph.getChildByName(localeName) != null);
        
        if (result) {
            ph.addFilterResult("L10N notes says DONT_TRANSLATE, but a translation exists [" +
                    ((Translation) ph.getChildByName(localeName)).getText() + "]");
        }
        
        return result;
    }
}
