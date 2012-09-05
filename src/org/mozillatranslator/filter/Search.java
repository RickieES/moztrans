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

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.kernel.Kernel;

/**
 * Filter to perform searches in the datamodel
 * @author  Henrik Lynggaard
 */
public class Search implements Filter {
    public static final int FIELD_KEY = 0;
    public static final int FIELD_ORG_TEXT = 1;
    public static final int FIELD_TRANS_TEXT = 2;
    public static final int FIELD_COMMENT = 3;
    public static final int FIELD_TRNS_STATUS = 4;
    public static final int RULE_IS = 0;
    public static final int RULE_IS_NOT = 1;
    public static final int RULE_CONTAINS = 2;
    public static final int RULE_CONTAINS_NOT = 3;
    public static final int RULE_STARTS_WITH = 4;
    public static final int RULE_ENDS_WITH = 5;
    public static final int RULE_REGULAR_EXPRESSION = 6;
    private String localeName;
    private int rule;
    private int field;
    private String value;
    private boolean caseCheck;
    private boolean koAsTranslated;
    private Pattern p;

    /**
     * Creates a new Search filter object
     * @param ln locale code
     * @param r an integer from the RULE_* constants defined in this class
     * @param f an integer from the FIELD_* constants defined in this class
     * @param v value to search from
     * @param cc true if the search must be case sensitive
     * @param ko true if the search must handle Original strings as Translated
     * when Keep Original flag is set in the Phrase and the search takes place
     * in the Translated field
     */
    public Search(String ln, int r, int f, String v, boolean cc, boolean ko) {
        localeName = ln;

        rule = r;
        field = f;
        value = v;
        caseCheck = cc;
        koAsTranslated = ko;

        if (rule == RULE_REGULAR_EXPRESSION) {
            int matchFlags = caseCheck ? 0 : Pattern.CASE_INSENSITIVE;
            try {
                p = Pattern.compile(value, matchFlags);
            } catch (PatternSyntaxException e) {
                Kernel.appLog.log(Level.INFO,
                        "Invalid regular expression, no search performed");
                p = null;
            } catch (IllegalArgumentException e) {
                Kernel.appLog.log(Level.INFO,
                        "Error in the program");
                p = null;
            }
        }
    }

    @Override
    public boolean check(Phrase ph) {
        boolean result = false;
        String compareText = null;
        Translation currentTranslation = (Translation) ph.getChildByName(localeName);

        switch (field) {
            case FIELD_KEY:
                compareText = ph.getName();
                break;
            case FIELD_ORG_TEXT:
                compareText = ph.getText();
                break;
            case FIELD_TRANS_TEXT:
                if (currentTranslation != null) {
                    compareText = currentTranslation.getText();
                } else if ((koAsTranslated) && (ph.isKeepOriginal())) {
                    compareText = ph.getText();
                }
                break;
            case FIELD_COMMENT:
                if (currentTranslation != null) {
                    compareText = currentTranslation.getComment();
                }
                break;
            case FIELD_TRNS_STATUS:
                if (currentTranslation != null) {
                    compareText = currentTranslation.getStatus().toString();
                }
                break;
        }

        if (compareText != null) {
            if (!caseCheck && rule != RULE_REGULAR_EXPRESSION) {
                compareText = compareText.toLowerCase();
                value = value.toLowerCase();
            }
            switch (rule) {
                case RULE_IS:
                    result = compareText.equals(value);
                    break;
                case RULE_IS_NOT:
                    result = !compareText.equals(value);
                    break;
                case RULE_CONTAINS:
                    result = (compareText.indexOf(value) != -1);
                    break;
                case RULE_CONTAINS_NOT:
                    result = (compareText.indexOf(value) == -1);
                    break;
                case RULE_STARTS_WITH:
                    result = compareText.startsWith(value);
                    break;
                case RULE_ENDS_WITH:
                    result = compareText.endsWith(value);
                    break;
                case RULE_REGULAR_EXPRESSION:
                    if (p != null) {
                        Matcher m = p.matcher(compareText);
                        result = m.matches();
                    } else {
                        result = false;
                    }
                    break;
            }
        }
        return result;
    }

    /**
     * Returns the locale code
     * @return a string with the locale code
     */
    public String getLocaleName() {
        return localeName;
    }
}
