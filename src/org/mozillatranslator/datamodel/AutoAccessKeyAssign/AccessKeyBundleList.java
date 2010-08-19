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
 */

package org.mozillatranslator.datamodel.AutoAccessKeyAssign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 * List of AccessKeyBundle items
 * @author rpalomares
 */
public class AccessKeyBundleList extends ArrayList<AccessKeyBundle> {
    private boolean onlyFuzzies;
    private boolean keepExistingAssigns;
    private boolean honorOriginalIfPossible;
    private String charList;
    private String badCharList;
    private String locale;
    
    /**
     * Default constructor
     * @param locale locale code in which we are working
     */
    public AccessKeyBundleList(String locale) {
        super();
        this.locale = locale;
    }
    
    /**
     * Iterates through the list to ensure that every item is an accesskey
     */
    public void cleanList() {
        Iterator<AccessKeyBundle> it = this.iterator();
        
        while (it.hasNext()) {
            AccessKeyBundle akb = it.next();
            Phrase ph = akb.getOriginal();
            if (!ph.isAccesskey()) {
                it.remove();
            }
        }
    }
    
    /**
     * Shuffles the list
     */
    public void shuffle() {
        Collections.shuffle(this);
    }
    
    /**
     * Mark all items in the list as fuzzy
     */
    public void markAllAsFuzzy() {
        Iterator<AccessKeyBundle> it = this.iterator();
        
        while (it.hasNext()) {
            AccessKeyBundle akb = it.next();
            akb.setFuzzy(true);
        }
    }
    
    /**
     * Initializes all proposed values for accesskeys in the AccesKeyBundle
     * items of this list
     */
    public void cleanPreviousAssignments() {
        Iterator<AccessKeyBundle> it = this.iterator();
        
        while (it.hasNext()) {
            AccessKeyBundle akb = it.next();
            if (akb.isFuzzy()) {
                akb.setProposedValue("");
            }
        }
    }
    
    /**
     * Adjusts the character list, removing already assigned accesskeys
     */
    public void adjustCharList() {
        Iterator<AccessKeyBundle> it = this.iterator();
        
        while (it.hasNext()) {
            AccessKeyBundle akb = it.next();
            
            if ((akb.getProposedValue().length() > 0)
                    && (charList.indexOf(akb.getProposedValue()) != -1)) {
                removeCharFromList(akb.getProposedValue(), false, false);
            }
        }
    }
    
    /**
     * Makes a copy of the object
     * @return a fresh copy of this AccessKeyBundleList
     */
    public AccessKeyBundleList makeCopy() {
        AccessKeyBundleList clonedCopy;
        
        clonedCopy = new AccessKeyBundleList(this.locale);
        
        Collections.copy(clonedCopy, this);
        clonedCopy.onlyFuzzies = this.onlyFuzzies;
        clonedCopy.keepExistingAssigns = this.keepExistingAssigns;
        clonedCopy.honorOriginalIfPossible = this.honorOriginalIfPossible;
        clonedCopy.charList = charList;
        return clonedCopy;
    }
    
    /**
     * Removes charItem from charList, optionally in a non-sensitive case way,
     * so if* charItem is "a", both "A" and "a" will be removed from charList
     * @param charItem the item to remove
     * @param caseSensitive true if the search and removal must be case-sensitive
     * @param justBlank true if the method should blank the character, not remove it
     * @return the number of characters removed/blanked from charList
     */
    public int removeCharFromList(String charItem, boolean caseSensitive, boolean justBlank) {
        StringBuilder sb = new StringBuilder(charList);
        String refinedCharItem = charItem.substring(0, 1);
        int index;
        int removedChars = 0;
        
        index = sb.indexOf((caseSensitive) ? refinedCharItem : refinedCharItem.toUpperCase());
        if (index != -1) {
            if (justBlank) {
                sb.replace(index, index + 1, " ");
            } else {
                sb.deleteCharAt(index);
            }
            removedChars++;
        }

        index = sb.indexOf((caseSensitive) ? refinedCharItem : refinedCharItem.toLowerCase());
        if (index != -1) {
            if (justBlank) {
                sb.replace(index, index + 1, " ");
            } else {
                sb.deleteCharAt(index);
            }
            removedChars++;
        }
        charList = sb.toString();
        return removedChars;
    }
    
    /**
     * Assigns accesskeys following existing rules
     * @return true if the assignment has completed successfully
     */
    public boolean autoAssign() {
        int pendingAKeys;
        int pendingItems;
        Iterator<AccessKeyBundle> it;
        
        onlyFuzzies = Kernel.settings.getBoolean(Settings.AUTOAA_ONLY_FUZZIES);
        keepExistingAssigns = Kernel.settings.getBoolean(Settings.AUTOAA_KEEP_EXISTING);
        honorOriginalIfPossible = Kernel.settings.getBoolean(Settings.AUTOAA_HONOR_ORIGINAL);
        charList = Kernel.settings.getString(Settings.AUTOAA_CHAR_LIST);
        badCharList = Kernel.settings.getString(Settings.AUTOAA_BAD_CHARS);
        
        if (!onlyFuzzies) {
            markAllAsFuzzy();
        }
        
        if (!keepExistingAssigns) {
            cleanPreviousAssignments();
        }
        
        // Removes already assigned accesskeys
        adjustCharList();
        
        // Count how many accesskeys are candidates to be auto-assigned
        pendingItems = 0;
        
        if (honorOriginalIfPossible) {
            it = this.iterator();
            while (it.hasNext()) {
                AccessKeyBundle akb = it.next();

                if ((badCharList.indexOf(akb.getOriginal().getText()) == -1)
                        && (akb.isInLabel(akb.getOriginal().getText()))) {
                    akb.setProposedValue(akb.getOriginal().getText());
                    akb.setFuzzy(false);
                    removeCharFromList(akb.getProposedValue(), false, false);
                }
            }
        }

        it = this.iterator();
        while (it.hasNext()) {
            AccessKeyBundle akb = it.next();
            if (akb.getProposedValue().length() == 0) {
                pendingItems++;
            }
        }
        
        pendingAKeys = charList.length();
        
        /** The actual loop to auto-assign accesskeys
         * 
         * The loop iterates over the *character list*, not the accesskeys
         * list. It is done this way to ensure the accesskeys are assigned
         * the best available character at any time.
         */
        for(int i = 0; i < pendingAKeys; i++) {
            String candidateChar = charList.substring(i, i + 1);

            if (!candidateChar.equals(" ")) {
                boolean assigned = false;

                // Look for the first pending item that could use the character
                it = this.iterator();
                while (it.hasNext() && !assigned) {
                    AccessKeyBundle akb = it.next();

                    if (akb.isFuzzy() && akb.isInLabel(candidateChar)) {
                        // Assign the character to the item
                        akb.setProposedValue(candidateChar);
                        // Mark the item as no longer pending
                        akb.setFuzzy(false);
                        pendingItems--;
                        removeCharFromList(candidateChar, false, true);
                        assigned = true;
                    }
                }

                // If we haven't assigned the character to any accesskey, we just
                // remove it in a case-sensitive way
                if (!assigned) {
                    removeCharFromList(candidateChar, true, true);
                }

                // If there is no remaining accesskeys to be assigned
                if (pendingItems == 0) {
                    // Force exiting the loop
                    i = pendingAKeys;
                }
            }
        }
        return (pendingItems == 0);
    }
    
    /**
     * Apply current assignments to the underlying accesskeys
     */
    public void applyAutoAssign() {
        String proposedValue;
        String originalValue;
        
        Iterator<AccessKeyBundle> it = this.iterator();
        while (it.hasNext()) {
            AccessKeyBundle akb = it.next();
            
            try {
                proposedValue = akb.getProposedValue().substring(0, 1);
            } catch (NullPointerException e) {
                proposedValue = "";
            } catch (StringIndexOutOfBoundsException e) {
                proposedValue = "";
            }
            
            try {
                originalValue = akb.getOriginal().getText().substring(0, 1);
            } catch (NullPointerException e) {
                originalValue = "";
            } catch (StringIndexOutOfBoundsException e) {
                originalValue = "";
            }
            
            // If proposedValue is the same than originalValue, we must remove
            // the Translation child and set keepOriginal
            if (proposedValue.equals(originalValue)) {
                akb.getOriginal().removeChild(akb.getOriginal().getChildByName(locale));
                akb.getOriginal().setKeepOriginal(true);
            } else {
                Translation t = akb.getAKeyItem();
                if (t == null) {
                    t = new Translation(this.locale, akb.getOriginal(),
                                        akb.getProposedValue(),
                                        Translation.STATUS_TRANSLATED);
                    akb.getOriginal().setKeepOriginal(false);
                    akb.getOriginal().addChild(t);
                }
                t.setText(akb.getProposedValue());
            }
            akb.getOriginal().setFuzzy(false);
        }
    }
}
