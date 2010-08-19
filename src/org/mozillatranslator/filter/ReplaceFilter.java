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
 * Implements a filter to select strings to be replaced
 *
 * @author rpalomares
 */
public class ReplaceFilter implements Filter {
    private String localeName;
    private String replaceWhat;
    private String replaceTo;
    private String orText;
    private int origRule;
    private boolean caseSense;
    private boolean exactMatch;
    
    private static final int OR_RUL_DONTCARE = 0;
    private static final int OR_RUL_CONTAINS = 1;
    private static final int OR_RUL_NOT_CONT = 2;
    private static final int OR_RUL_IS_EQUAL = 3;
    private static final int OR_RUL_ISNT_EQU = 4;
    
    /**
     * Creates a new instance of a replace filter, which provides the needed tests
     * to search & replace strings in the translation
     *
     * @param ln        the locale name
     * @param what      the text to be searched upon
     * @param to        text to replace with every succesful occurrence of what
     * @param origRule  rule to apply to original text (orText); rules are defined
     *                  as private static final ints "OR_RUL_*"
     * @param orText    original text, in case it has to be tested per origRule
     * @param cs        true if the search has to be case sensitive
     * @param exact     true if this.what has to be equal to the translated text
     *                  to be replaced, false if it can be *part* of a translated
     *                  text
     */
    public ReplaceFilter(String ln, String what, String to, int origRule,
            String orText, boolean cs, boolean exact) {
        
        this.localeName = ln;
        this.replaceWhat = what;
        this.replaceTo = to;
        this.origRule = origRule;
        this.orText = orText;
        this.caseSense = cs;
        this.exactMatch = exact;
    }


    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    public boolean check(Phrase phrase) {
        Translation tn = (Translation) phrase.getChildByName(localeName);
        boolean result = (tn != null);
        
        // First, we check that the rules applied to the original text apply
        switch (origRule) {
            case OR_RUL_CONTAINS:
                result = result && ((caseSense) ?
                    (phrase.getText().indexOf(orText) > -1) :
                    (phrase.getText().toLowerCase().indexOf(orText.toLowerCase()) > -1));
                break;
            case OR_RUL_NOT_CONT:
                result = result && ((caseSense) ?
                    (phrase.getText().indexOf(orText) == -1) :
                    (phrase.getText().toLowerCase().indexOf(orText.toLowerCase()) == -1));
                break;
            case OR_RUL_IS_EQUAL:
                result = result && ((caseSense) ? (phrase.getText().equals(orText)) :
                    (phrase.getText().equalsIgnoreCase(orText)));
                break;
            case OR_RUL_ISNT_EQU:
                result = result && ((caseSense) ? (!phrase.getText().equals(orText)) :
                    (!phrase.getText().equalsIgnoreCase(orText)));
                break;
            default:
                // result keeps being result, ie. if there is no translation, it
                // will be false, otherwise it will be true
        }

        if (result) {
            if (exactMatch) {
                result = (caseSense) ? (tn.getText().equals(replaceWhat)) :
                    (tn.getText().equalsIgnoreCase(replaceWhat));
            } else {
                result = (caseSense) ?
                    (tn.getText().indexOf(replaceWhat) > -1) :
                    (tn.getText().toLowerCase().indexOf(replaceWhat.toLowerCase()) > -1);
            }
            
            if (result) {
                String transText = tn.getText();
                
                tn.setText(transText.replace(replaceWhat, replaceTo));
                phrase.addFilterResult("Original value [" + transText + "]");
            }
        }
        return result;
    }
}
