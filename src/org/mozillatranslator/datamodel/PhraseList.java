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
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * A list of phrases, used for a basic suggestions feature
 * @author rpalomares
 */
public class PhraseList extends ArrayList<Phrase> implements ListModel,
        TableModel {
    private static final String[] columnNames = {"Match %",
                                                 "Suggestion",
                                                 "en-US value"};
    private static final int[] columnWidths = {20, 200, 150};
    private int cycleIndex = -1;
    private String l10n;
    private List<ListDataListener> ldls = new ArrayList<ListDataListener>();
    private int[] matchPercentage;
    
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
            cycleIndex++;
            cycleIndex = (cycleIndex >= this.size()) ? 0 : cycleIndex;
            p = this.get(cycleIndex);
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
     * Sets the current value of the cycle index for suggestions
     * @param cycleIndex the value of the cycle index to be set
     */
    public void setCycleIndex(int cycleIndex) {
        this.cycleIndex = cycleIndex;
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

    @Override
    public int getSize() {
        return this.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (this.get(index).getChildByName(l10n) != null) {
            return ((Translation) this.get(index).getChildByName(l10n)).getText();
        } else {
            return "(Keep Original flag set)";
        }
    }
    
    @Override
    public void addListDataListener(ListDataListener l) {
        this.ldls.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        this.ldls.remove(l);
    }

    /**
     * Calculates the matching percentage of all suggestions for the phrase
     * passed in and sorts the PhraseList based on its percentage (values range
     * from 0 to 100)
     * 
     * @param p the prase for which we want to calculate the matching percentage
     */
    public void calculateMatchPercentage(Phrase p) {
        this.matchPercentage = new int[this.getSize()];
        int referenceLength = p.getText().length();
        int levDistance;

        for(int i = 0; i < this.matchPercentage.length; i++) {
            levDistance = TranslationSuggestions.getLevenshteinDistance(p.getText(),
                            this.get(i).getText());
            this.matchPercentage[i] = ((referenceLength - levDistance) * 100 / referenceLength);
        }
        this.sortOnMatchPercentageValue();
    }

    /**
     * Sets the matching percentage of a suggestion; the value is adjusted to the
     * range 0-100
     * @param index the suggestion index
     * @param value the matching percentage to set
     */
    public void setMatchPercentage(int index, int value) {
        this.matchPercentage[index] = Math.min(Math.max(0, value), 100);
    }

    /**
     * Gets the matching percentage of a suggestion
     * @param index the suggestion index
     * @return value the matching percentage of the suggestion in the range 0-100
     */
    public int getMatchPercentage(int index) {
        return this.matchPercentage[index];
    }

    /**
     * Gets the matching percentage of the current suggestion in the cycle
     * @return value the matching percentage of the current suggestion in the cycle, in the range 0-100
     */
    public int getCurrentMatchPercentage() {
        return getMatchPercentage(this.cycleIndex);
    }

    /**
     * Sorts both the underlying ArrayList<Phrase> and the percentage array
     * based on descending percentage values
     */
    public void sortOnMatchPercentageValue() {
        // We use the selection algorithm, since most of the time we will be
        // dealing with tiny lists with less than 10 elements
        for(int i = 0; i < this.matchPercentage.length - 1; i++) {
            int max = this.matchPercentage[i];
            int pos = i;
            for(int j = i; j < this.matchPercentage.length; j++) {
                if (max < this.matchPercentage[j]) {
                    max = this.matchPercentage[j];
                    pos = j;
                }
            }
            // Exchange items, not only in the matchPercentage array, but
            // the corresponding Phrase objects, too
            int tempValue = this.matchPercentage[i];
            this.matchPercentage[i] = this.matchPercentage[pos];
            this.matchPercentage[pos] = tempValue;
            Phrase tempPhrase = this.get(i);
            this.set(i, this.get(pos));
            this.set(pos, tempPhrase);
        }
   }

    @Override
    public int getRowCount() {
        return this.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if ((columnIndex >=0) && (columnIndex <= columnNames.length)) {
            return columnNames[columnIndex];
        } else {
            return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Phrase item = this.get(rowIndex);
            switch (columnIndex) {
                case 0: // Match percentage
                    return this.getMatchPercentage(rowIndex);
                case 1: // Suggestion value
                    if (item.getChildByName(l10n) != null) {
                        return ((Translation) item.getChildByName(l10n)).getText();
                    } else {
                        return item.getText() + " (Keep Original)";
                    }
                case 2: // Original en-US value
                    return item.getText();
                default:
                    return "";
            }
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * Returns the preferred column widths
     * @param index the column index whose preferred width we want to know
     * @return the preferred index for the column[idx], or 50 for unknown columns
     */
    public int getColumnDefaultWidth(int index) {
        if ((index >=0) && (index <= columnWidths.length)) {
            return columnWidths[index];
        } else {
            return 50;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
