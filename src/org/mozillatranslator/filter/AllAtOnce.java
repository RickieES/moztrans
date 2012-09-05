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

/**
 * Filter to cover all individual filters considered "Basic automated QA"
 * @author rpalomares
 */
public class AllAtOnce implements Filter {
    private Untranslated    fltUntranslated;
    private KeepOriginal    fltKeepOriginal;
    private Identical       fltIdentical;
    private CheckVariables  fltCheckVariables;
    private CheckKeybinding fltCheckKeybinding;
    private DontTranslate   fltDontTranslate;
    private CheckEndings    fltCheckEndings;
    private CheckDuplicated fltCheckDuplicated;

    /** Creates a new instance of AllAtOnce
     * @param ln Locale code
     */
    public AllAtOnce(String ln) {
        fltUntranslated    = new Untranslated(ln);
        fltKeepOriginal    = new KeepOriginal(ln);
        fltIdentical       = new Identical(ln);
        fltCheckVariables  = new CheckVariables(ln);
        fltCheckKeybinding = new CheckKeybinding(ln);
        fltDontTranslate   = new DontTranslate(ln);
        fltCheckEndings    = new CheckEndings(ln);
        fltCheckDuplicated = new CheckDuplicated(ln);
    }

    @Override
    public boolean check(Phrase ph) {
        boolean result, resultSingle;

        /* We want ALL filters to be run, but if we do something like
         *
         * result = (filter1(ph) || filter2(ph) || ...)
         *
         * as soon as one of them return true, result will be true, so
         * Java will shortcircuit the logical OR and won't run the remaining
         * filters. That's why we use the resultSingle / result variable pair
         */
        
        resultSingle = fltUntranslated.check(ph);
        result = resultSingle;

        resultSingle = fltKeepOriginal.check(ph);
        result = result || resultSingle;

        resultSingle = fltIdentical.check(ph);
        result = result || resultSingle;

        resultSingle = fltCheckVariables.check(ph);
        result = result || resultSingle;

        resultSingle = fltCheckKeybinding.check(ph);
        result = result || resultSingle;

        resultSingle = fltDontTranslate.check(ph);
        result = result || resultSingle;

        resultSingle = fltCheckEndings.check(ph);
        result = result || resultSingle;

        resultSingle = fltCheckDuplicated.check(ph);
        result = result || resultSingle;

        return result;
    }
}
