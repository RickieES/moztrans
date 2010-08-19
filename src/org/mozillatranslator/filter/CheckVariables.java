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

import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 *
 * @author rpalomares
 */
public class CheckVariables implements Filter {
    private String localeName;

    /** Creates a new instance of CheckVariables */
    public CheckVariables(String ln) {
        this.localeName = ln;
    }

    public boolean check(Phrase ph) {
        final String PARAMETER = "Parameter [";
        final String MORE_ERRORS_COULD_HAPPEN = "] not found, more errors could happen";
        final String NUMBER_OF = "Number of ";
        final String DIFFERS_BETWEEN = " differs between original and translation";
        boolean result = true; // This tests %S check and holds global result
        boolean result2 = true; // This tests %1$S, %2$S, etc.
        boolean result3 = true; // This tests ${[:word:]}
        boolean result4 = true; // This tests %name%
        Pattern p;
        Matcher mOrig;
        Matcher mTrns;
        TreeSet tsOrig; // To save %1$S, %2$S, etc. present in original string in a sorted way
        TreeSet tsTrns; // Same for translation
        Iterator iterOrig; // To iterate over tsOrig
        Iterator iterTrns; // To iterate over tsTrns
        String txtOrig; // The original string
        String txtTrns; // The translation
        String subsOrig; // Every %1$S, %2$S, etc. in original string (items from tsOrig)
        String subsTrns; // Same for translation (items from tsTrns)
        Settings set = Kernel.settings;
        
        String origExclussions = set.getString(Settings.QA_DTD_ORIG_ENTITIES_IGNORED);
        String trnsExclussions = set.getString(Settings.QA_DTD_TRNS_ENTITIES_IGNORED);

        txtOrig = ph.getText();
        
        if ((!ph.isKeepOriginal()) && (ph.getChildByName(localeName) != null)) {
            
            // Checks for Properties files are more complete than for DTD files
            if (ph.getParent() instanceof PropertiesFile) {
                txtTrns = ((Translation) ph.getChildByName(localeName)).getText();
                // Test for %S coincidence
                p = Pattern.compile("%S");
                mOrig = p.matcher(txtOrig);
                mTrns = p.matcher(txtTrns);
                while (mOrig.find()) {
                    result = result && mTrns.find();
                }
                if (!result) {
                    ph.addFilterResult(NUMBER_OF + "%S parameters mismatch");
                }
                
                // Test for %n$S coincidence
                p = null; mOrig = null; mTrns = null;
                p = Pattern.compile("%\\d\\$S");
                mOrig = p.matcher(txtOrig);
                mTrns = p.matcher(txtTrns);
                tsOrig = new TreeSet();
                tsTrns = new TreeSet();
                
                while (mOrig.find()) {
                    tsOrig.add(txtOrig.substring(mOrig.start(), mOrig.end()));
                }
                
                while (mTrns.find()) {
                    tsTrns.add(txtTrns.substring(mTrns.start(), mTrns.end()));
                }
                
                result2 = (tsOrig.size() == tsTrns.size());
                
                if (result2) {
                    iterOrig = tsOrig.iterator();
                    iterTrns = tsTrns.iterator();
                    
                    while (result2 && (iterOrig.hasNext())) {
                        subsOrig = (String) iterOrig.next();
                        subsTrns = (String) iterTrns.next();
                        
                        if (!subsOrig.equals(subsTrns)) {
                            result2 = false;
                            ph.addFilterResult(PARAMETER + subsOrig +
                                               MORE_ERRORS_COULD_HAPPEN);
                        }
                    }
                } else {
                    ph.addFilterResult(NUMBER_OF + "parameters %n$S" + DIFFERS_BETWEEN);
                }
                
                result = result && result2;
                
                // Test for ${word} coincidence
                p = null; mOrig = null; mTrns = null;
                p = Pattern.compile("\\$\\{\\w+\\}");
                mOrig = p.matcher(txtOrig);
                mTrns = p.matcher(txtTrns);
                tsOrig = new TreeSet();
                tsTrns = new TreeSet();
                
                while (mOrig.find()) {
                    tsOrig.add(txtOrig.substring(mOrig.start(), mOrig.end()));
                }
                
                while (mTrns.find()) {
                    tsTrns.add(txtTrns.substring(mTrns.start(), mTrns.end()));
                }

                result3 = (tsOrig.size() == tsTrns.size());
                
                if (result3) {
                    iterOrig = tsOrig.iterator();
                    iterTrns = tsTrns.iterator();
                    
                    while (result3 && (iterOrig.hasNext())) {
                        subsOrig = (String) iterOrig.next();
                        subsTrns = (String) iterTrns.next();
                        
                        if (!subsOrig.equals(subsTrns)) {
                            result3 = false;
                            ph.addFilterResult(PARAMETER + subsOrig +
                                               MORE_ERRORS_COULD_HAPPEN);
                        }
                    }
                } else {
                    ph.addFilterResult(NUMBER_OF + "${word}" + DIFFERS_BETWEEN);
                }
                
                result = result && result3;

                // Test for %word% coincidence
                p = null; mOrig = null; mTrns = null;
                p = Pattern.compile("%\\w+%");
                mOrig = p.matcher(txtOrig);
                mTrns = p.matcher(txtTrns);
                tsOrig = new TreeSet();
                tsTrns = new TreeSet();
                
                while (mOrig.find()) {
                    tsOrig.add(txtOrig.substring(mOrig.start(), mOrig.end()));
                }
                
                while (mTrns.find()) {
                    tsTrns.add(txtTrns.substring(mTrns.start(), mTrns.end()));
                }

                result4 = (tsOrig.size() == tsTrns.size());
                
                if (result4) {
                    iterOrig = tsOrig.iterator();
                    iterTrns = tsTrns.iterator();
                    
                    while (result4 && (iterOrig.hasNext())) {
                        subsOrig = (String) iterOrig.next();
                        subsTrns = (String) iterTrns.next();
                        
                        if (!subsOrig.equals(subsTrns)) {
                            result4 = false;
                            ph.addFilterResult(PARAMETER + subsOrig +
                                               MORE_ERRORS_COULD_HAPPEN);
                        }
                    }
                } else {
                    ph.addFilterResult(NUMBER_OF + "parameters %name%" + DIFFERS_BETWEEN);
                }
                
                result = result && result4;
            } else if (ph.getParent() instanceof DTDFile) {
                /* If the original text contains entities, we look for the same
                 * entities in the translated text.
                 */
                
                txtTrns = ((Translation) ph.getChildByName(localeName)).getText();
                
                // Test for &entityName.maybe.With.dots; coincidences
                p = null; mOrig = null; mTrns = null;
                p = Pattern.compile("&[\\w\\.]+;");
                mOrig = p.matcher(txtOrig);
                mTrns = p.matcher(txtTrns);
                tsOrig = new TreeSet();
                tsTrns = new TreeSet();
                
                while (mOrig.find()) {
                    if (origExclussions.indexOf(
                            txtOrig.substring(mOrig.start(), mOrig.end())) == -1) {
                        tsOrig.add(txtOrig.substring(mOrig.start(), mOrig.end()));
                    }
                }
                
                while (mTrns.find()) {
                    if (trnsExclussions.indexOf(
                            txtTrns.substring(mTrns.start(), mTrns.end())) == -1) {
                        tsTrns.add(txtTrns.substring(mTrns.start(), mTrns.end()));
                    }
                }
                
                result = (tsOrig.size() == tsTrns.size());
                
                if (result) {
                    iterOrig = tsOrig.iterator();
                    iterTrns = tsTrns.iterator();
                    
                    while (result && (iterOrig.hasNext())) {
                        subsOrig = (String) iterOrig.next();
                        subsTrns = (String) iterTrns.next();
                        
                        if (!subsOrig.equals(subsTrns)) {
                            result = false;
                            ph.addFilterResult("Entity [" + subsOrig +
                                               MORE_ERRORS_COULD_HAPPEN);
                        }
                    }
                } else {
                    ph.addFilterResult(NUMBER_OF + "DTD entities" + DIFFERS_BETWEEN);
                }
            }
        }

        // We actually want to mark when an error exists, so we need to negate the
        // result
        return !result;
    }
}
