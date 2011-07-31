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

/**
 * Filter to implement advanced search
 * @author  Serhiy Brytskyy
 * @version 1.0
 */
public class AdvancedSearch implements Filter {
    private String localeName;
    private int rule1;
    private int field1;
    private String value1;
    private boolean caseCheck1;
    private int rule2;
    private int field2;
    private String value2;
    private boolean caseCheck2;
    private int rule3;
    private int field3;
    private String value3;
    private boolean caseCheck3;
    private Search s1;
    private Search s2;
    private Search s3;
    private boolean all;
    private boolean b1;
    private boolean b2;
    private boolean b3;

    /**
     * Constructor for AdvancedSearch class
     * @param all true if the conditions are handled in AND way; false if in OR way
     * @param ln locale code
     * @param b1 true if first condition is to be applied
     * @param r1 type of rule for the first condition, as defined in Search.RULE_* constants
     * @param f1 field for the first condition, as defined in Search.FIELD_* constants
     * @param v1 value of the first condition
     * @param cc1 true if the search for first condition is to be done in a case sensitive way
     * @param b2 true if second condition is to be applied
     * @param r2 type of rule for the second condition, as defined in Search.RULE_* constants
     * @param f2 field for the second condition, as defined in Search.FIELD_* constants
     * @param v2 value of the second condition
     * @param cc2 true if the search for second condition is to be done in a case sensitive way
     * @param b3 true if third condition is to be applied
     * @param r3 type of rule for the third condition, as defined in Search.RULE_* constants
     * @param f3 field for the third condition, as defined in Search.FIELD_* constants
     * @param v3 value of the third condition
     * @param cc3 true if the search for third condition is to be done in a case sensitive way
     */
    public AdvancedSearch(boolean all, String ln, boolean b1, int r1, int f1,
                          String v1, boolean cc1, boolean b2, int r2, int f2,
                          String v2, boolean cc2, boolean b3, int r3, int f3,
                          String v3, boolean cc3) {
        this.all = all;
        this.localeName = ln;
        this.rule1 = r1; this.field1 = f1; this.value1 = v1; this.caseCheck1 = cc1;
        this.rule2 = r2; this.field2 = f2; this.value2 = v2; this.caseCheck2 = cc2;
        this.rule3 = r3; this.field3 = f3; this.value3 = v3; this.caseCheck3 = cc3;

        this.b1 = b1; this.b2 = b2; this.b3 = b3;

        s1 = new Search(ln, r1, f1, v1, cc1, false);
        s2 = new Search(ln, r2, f2, v2, cc2, false);
        s3 = new Search(ln, r3, f3, v3, cc3, false);
    }

    @Override
    public boolean check(Phrase ph) {
        boolean result = false;
        if (all) {
            if (b1) {
                result = s1.check(ph);
                if (b2) {
                    result = result && s2.check(ph);
                    if (b3) {
                        result = result && s3.check(ph);
                    }
                }
            }
        } else {
            if (b1) {
                result = s1.check(ph);
                if (b2) {
                    result = result || s2.check(ph);
                    if (b3) {
                        result = result || s3.check(ph);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the selected locale code
     * @return the locale code for which the filter will be applied
     */
    public String getLocaleName() {
        return localeName;
    }
}