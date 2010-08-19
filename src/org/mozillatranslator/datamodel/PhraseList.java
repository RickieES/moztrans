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

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * A list of phrases, used for a basic suggestions feature
 * @author rpalomares
 */
public class PhraseList extends ArrayList<Phrase> implements ListModel {
    private int cycleIndex = 0;
    private String l10n;
    private List<ListDataListener> ldls = new ArrayList<ListDataListener>();
    
    /**
     * Especial constructor for this class
     * @param l10n the L10n code we will be working with
     */
    public PhraseList(String l10n) {
        super();
        this.l10n = l10n;
    }
    
    /**
     * Returns the array index of the phrase passed as an argument
     * It looks for the <b>exact</b> object/instance, not the equality
     * of two any objects
     * @param p the phrase to look for
     * @param byTextValue if true, the search is done on the text value, not the
     * object itself
     * @return the index of the phrase, or -1 if the prase is not in the array
     */
    public int indexOfThisPhrase(Phrase p, boolean byTextValue) {
        int idx = 0;
        
        while ((idx < this.size()) && (((p != this.get(idx)) && (!byTextValue))
                || (!p.getText().equalsIgnoreCase(this.get(idx).getText())))) {
            idx++;
        }
        return (idx == this.size()) ? -1 : idx;
    }
    
    /**
     * Returns the next element in the list
     * @return the next Phrase in the list, or null if the list is empty
     */
    public Phrase cycleThroughList() {
        Phrase p = null;
        
        // If the list has at least one element
        if (this.size() > 0) {
            // The list could have been reduced since the last time we cycle
            // on it, so we make sure cycleIndex is not outside the list bounds
            cycleIndex = (cycleIndex >= this.size()) ? 0 : cycleIndex;
            p = this.get(cycleIndex);
            cycleIndex++;
            cycleIndex = (cycleIndex >= this.size()) ? 0 : cycleIndex;
        }
        return p;
    }
    
    /**
     * Returns the current value of the cycle index for suggestions
     * @return the current value of the cycle index
     */
    public int getCurrentIndex() {
        return cycleIndex;
    }
    
    /**
     * Returns the first element in the list
     * @return the first Phrase in the list, or null if the list is empty
     */
    public Phrase getFirstElementInList() {
        Phrase p = null;
        
        // If the list has at least one element
        if (this.size() > 0) {
            cycleIndex = 0;
            p = this.get(cycleIndex);
        }
        return p;
    }

    public int getSize() {
        return this.size();
    }

    public Object getElementAt(int index) {
        if (this.get(index).getChildByName(l10n) != null) {
            return ((Translation) this.get(index).getChildByName(l10n)).getText();
        } else {
            return "(Keep Original flag set)";
        }
    }
    
    public void addListDataListener(ListDataListener l) {
        this.ldls.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        this.ldls.remove(l);
    }
}
