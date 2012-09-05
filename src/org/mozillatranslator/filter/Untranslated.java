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

import org.mozillatranslator.datamodel.BinaryFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class Untranslated implements Filter {
    private String localeName;

    /** Creates new Untranslated
     * @param ln Locale code
     */
    public Untranslated(String ln) {
        localeName = ln;
    }

    @Override
    public boolean check(Phrase ph) {
        boolean result = false;
        Translation currentTranslation;

        if (!(ph.getParent() instanceof BinaryFile)) {
            currentTranslation = (Translation) ph.getChildByName(localeName);
            result = ((currentTranslation == null) && (!ph.isKeepOriginal()));
            if (result) {
                ph.addFilterResult("String not translated nor kept like original");
            }
        }
        return result;
    }
}
