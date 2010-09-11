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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.util.Collections;

/**
 * This class keeps track of Phrases in an indexed way to be able to provide
 * translations suggestions 
 * @author rpalomares
 */
public class TranslationSuggestions implements PropertyChangeListener {
    private HashMap<Character, PhraseList> initialEntries;
    
    /**
     * Constructor for TranslationSuggestions
     */
    public TranslationSuggestions() {
        initialEntries = new HashMap<Character, PhraseList>();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Phrase origin = (Phrase) evt.getSource();
        String oldValue = (String) evt.getOldValue();
        String newValue = (String) evt.getNewValue();
        char oldInitial = ((oldValue == null) || (oldValue.length() == 0))
                          ? ' ' : oldValue.toLowerCase().charAt(0);
        char newInitial = ((newValue == null) || (newValue.length() == 0))
                          ? ' ' : newValue.toLowerCase().charAt(0);
        
        // If the initial character doesn't change, we don't need to move the
        // Phrase object from one ArrayList to another one
        if (oldInitial == newInitial) {
            // ...although, if the text itself has changed, we need to
            // sort again the ArrayList
            if ((oldValue != null) && (!oldValue.equalsIgnoreCase(newValue))) {
                ArrayList<Phrase> l = initialEntries.get(oldInitial);
                if (l != null) {
                    Collections.sort(l, Phrase.TEXT_ORDER);
                }
            }
        } else {
            // Remove Phrase from the list for oldInitial...
            removeFromIndexedList(origin, oldInitial);
            // ...and add it to the list for newInitial
            addToIndexedList(origin, newInitial);
        }
    }
    
    private void addToIndexedList(Phrase p, char c) {
        PhraseList pl = this.initialEntries.get(c);
        
        // If there is no list for this initial yet, add it
        if (pl == null) {
            pl = new PhraseList(null);
            this.initialEntries.put(c, pl);
        }
        
        pl.add(p);
        Collections.sort(pl, Phrase.TEXT_ORDER);
    }
    
    private void removeFromIndexedList(Phrase p, char c) {
        PhraseList pl = this.initialEntries.get(c);
        
        // This condition will likely always be true, unless we are creating
        // the structure on glossary loading
        if (pl != null) {
            int idx = pl.indexOfThisPhrase(p, false);
            
            // ...as will this one with the same exception, too
            if (idx != -1) {
                pl.remove(idx);
            }
        }
    }
    
    /**
     * Returns a list of Phrases with the same text than p
     * @param p the Phrase whose text we want a list of
     * @param l10n the locale which we are looking suggestions for
     * @param excludeOwn true if p must be excluded of the list
     * @return the list of Phrases (and, therefore, their Translations) for
     * suggestions
     */
    public PhraseList suggestionsForPhrase(Phrase p, String l10n,
                                           boolean excludeOwn) {
        PhraseList suggestions = null;
        char c;
        int idx;
        boolean include;
        
        // Get the index letter
        if ((p.getText() != null) && (p.getText().length() > 0)) {
            c = p.getText().toLowerCase().charAt(0);
        } else {
            c = ' ';
        }
        
        // Get the list for the index letter
        PhraseList pl = this.initialEntries.get(c);
        
        // If the list isn't empty and the phrase is in the list        
        if ((pl != null) && (pl.indexOfThisPhrase(p, true)) != -1) {
            // Get the index of the first phrase with the same original text
            idx = pl.indexOfThisPhrase(p, true);
            suggestions = new PhraseList(l10n);
            
            while ((idx < pl.size())
                   && (pl.get(idx).getText().equalsIgnoreCase(p.getText()))) {
                
                Phrase pInList = pl.get(idx);
                
                // If the current phrase is not p or we want to keep p in the list
                if (!(excludeOwn && (pInList == p))) {
                    // We don't want to include phrases without Translation
                    // children (unless Keep Original is set), nor with
                    // Translations without text in the passed L10n
                    Translation t = (Translation) pInList.getChildByName(l10n);
                    include = (t != null) && (t.getText() != null)
                              && (t.getText().length() > 0);
                    include = include || (pInList.isKeepOriginal());
                    if (include) {
                        suggestions.add(pl.get(idx));
                    }
                }
                idx++;
            }
        }
        
        return suggestions;
    }
}