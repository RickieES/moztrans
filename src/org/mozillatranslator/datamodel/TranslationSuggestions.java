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
 * Michael Gilleland and Chas Emerick (public domain Levenshtein Distance algorithm)
 * Ricardo Palomares (Initial Code)
 *
 */

package org.mozillatranslator.datamodel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

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
        boolean onlyExactMatches;
        Phrase pInList;

        // Get the index letter
        if ((p.getText() != null) && (p.getText().length() > 0)) {
            c = p.getText().toLowerCase().charAt(0);
        } else {
            c = ' ';
        }

        // Get the list for the index letter
        PhraseList pl = this.initialEntries.get(c);
        onlyExactMatches = (Kernel.settings.getInteger(Settings.SUGGESTIONS_MATCH_VALUE) == 100);
        idx = onlyExactMatches ? pl.indexOfThisPhrase(p, true) : 0;

        // If the list isn't empty and (the phrase is in the list, or we don't
        // want exact matches)
        if ((pl != null) && (idx != -1)) {
            // Create the list of suggestions to return
            suggestions = new PhraseList(l10n);

            while (idx < pl.size()) {

                if (onlyExactMatches) {
                    // If the phrase matches, case-insensitive
                    if (pl.get(idx).getText().equalsIgnoreCase(p.getText())) {
                        // Process the phrase
                        pInList = pl.get(idx);
                    } else {
                        // Provide a null phrase to avoid processing it,
                        pInList = null;
                        // and force exitting the while loop since the list is
                        // alphabetically sorted and we are not going to find
                        // any other match
                        idx = pl.size();
                    }
                } else {
                    // If the phrase approximates enough, process it
                    if (isCandidate(p.getText(), pl.get(idx).getText())) {
                        pInList = pl.get(idx);
                    } else {
                        // otherwise, ignore it and keep iterating (hence no
                        // idx = pl.size() needed)
                        pInList = null;
                    }
                }

                // If the current phrase is not p or we want to keep p in the list
                if ((pInList != null) && !(excludeOwn && (pInList == p))) {
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
            suggestions.calculateMatchPercentage(p);
        }
        return suggestions;
    }

    private boolean isCandidate(String s, String c) {
        int maxDistance = s.length() - (s.length() *
                          Kernel.settings.getInteger(Settings.SUGGESTIONS_MATCH_VALUE) / 100);
        int stringDistance = Math.max(s.length(), c.length()) - Math.min(s.length(), c.length());
        boolean result = (stringDistance <= maxDistance);

        if (result) {
            result = (maxDistance >= getLevenshteinDistance(s, c));
        }
        return result;
    }

    /**
     * Takes an ArrayList of Phrases and provides translations for them per
     * the following rules (see bug http://kenai.com/bugzilla/show_bug.cgi?id=4438):
     * <ul>
     * <li>Commandkeys will be flagged as Keep Original and no translation will
     *   be added.</li>
     * <li>Accesskeys will be left empty, as it is important than they are
     *   included in a bundle by the user through the Auto-Assign AccessKey
     *   dialog to propose better suggestions.</li>
     * <li>Phrases with DO_NOT_TRANSLATE associated comments will be left empty.
     *   They won't be marked instead as Keep Original because there are cases
     *   where this L10n note has been added to remark that specific parts of
     *   the original string, as opposed to the whole one, must be left
     *   untouched.</li>
     * <li>Phrases with existing translations will not be modified.</li>
     * </ul>
     * 
     * It also adjust the TrnsStatus value of the Translations provided.
     * 
     * @param pl a List of Phrases
     * @param locale a locale code
     */
    public void translatePhraseList(List<Phrase> pl, String locale) {
        for (Phrase phrase : pl) {
            // If there is no existing Translation
            if (phrase.getChildByName(locale) == null) {
                if (phrase.isCommandkey()) {
                    phrase.setKeepOriginal(true);
                } else if (phrase.isAccesskey()) {
                    // If the Phrase in this list to auto-translate has a
                    // Translation child, let's mark it as Untranslated
                    if (phrase.getChildByName(locale) != null) {
                        ((Translation) phrase.getChildByName(locale))
                                .setStatus(TrnsStatus.Untranslated);
                    }
                } else if ((phrase.getLocalizationNote() != null)
                            && phrase.getLocalizationNote().contains("DO_NOT_TRANSLATE")) {
                    // If the Phrase in this list to auto-translate has a
                    // Translation child, let's mark it as Untranslated
                    if (phrase.getChildByName(locale) != null) {
                        ((Translation) phrase.getChildByName(locale))
                                .setStatus(TrnsStatus.Untranslated);
                    }
                } else {
                    PhraseList suggList = this.suggestionsForPhrase(phrase, 
                                                                locale, true);
                    if ((suggList != null) && (suggList.getRowCount() > 0)) {
                        suggList.sortOnMatchPercentageValue();
                        // Now we have the suggestions list sorted on descending
                        // percentage value. We will always use the best possible
                        // suggestion and set the translation status correspondingly
                        // (see TrnsStatus enumeration for details)
                        Phrase bestSuggestion = suggList.getFirstElementInList();

                        phrase.setKeepOriginal(bestSuggestion.isKeepOriginal());
                        if (bestSuggestion.getChildByName(locale) != null) {
                            Translation t = (Translation) bestSuggestion.getChildByName(locale);
                            TrnsStatus tStatus;

                            if (suggList.getCurrentMatchPercentage() == 100) {
                                tStatus = (suggList.getRowCount() == 1) ?
                                        TrnsStatus.Copied : TrnsStatus.Proposed;
                            } else {
                                tStatus = TrnsStatus.Approximated;
                            }

                            phrase.addChild(new Translation(locale, phrase,
                                            t.getText(), tStatus));
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * Levenshtein distance algorithm
     * Placed in the public domain by Michael Gilleland and Chas Emerick
     *
     * http://www.merriampark.com/ld.htm
     * http://www.merriampark.com/ldjava.htm
     *
     * @param s First string to compare
     * @param t Second string to compare
     * @return the distance between s and t
     */
    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
        The difference between this impl. and the standard one is that, rather
        than creating and retaining a matrix of size s.length()+1 by t.length()+1,
        we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
        is the 'current working' distance array that maintains the newest distance cost
        counts as we iterate through the characters of String s.  Each time we increment
        the index of String t we are comparing, d is copied to p, the second int[].  Doing so
        allows us to retain the previous cost counts as required by the algorithm (taking
        the minimum of the cost count to the left, up one, and diagonally up and to the left
        of the current cost count being calculated).  (Note that the arrays aren't really
        copied anymore, just switched...this is clearly much better than cloning an array
        or doing a System.arraycopy() each time  through the outer loop.)

        Effectively, the difference between the two implementations is this one does not
        cause an out of memory condition when calculating the LD over two very large strings.
         */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }
}