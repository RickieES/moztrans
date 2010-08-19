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

package org.mozillatranslator.gui;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;
import org.mozillatranslator.datamodel.*;

/**
 * Interface for JInternalFrames used in MozillaTranslator
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public interface MozFrame {
    /**
     * Returns the Phrase behind the single selected row of the current MozFrame
     * @return a Phrase object showed in the selected row of a JTable of this frame
     */
    public Phrase getSelectedPhrase();
    
    /**
     * Returns an ArrayList of Phrase objects corresponding to selected rows
     * @return an ArrayList of Phrase objects corresponding to selected rows, or
     * null if no row is selected
     */
    public ArrayList<Phrase> getSelectedPhrases();

    /**
     * Returns the current locale code being used in this MozFrame instance
     * @return the current locale code being used in this MozFrame instance
     */
    public String getLocalization();

    /**
     * Returns a pointer to the JTable of this MozFrame
     * @return the JTable inside this MozFrame
     */
    public JTable getTable();

    /**
     * Reports whether the MozFrame includes a JTree or not
     * @return true if the MozFrame includes a JTree
     */
    public boolean hasTreeStructure();

    /**
     * Returns the current selected node in the JTree
     * @return a TreePath with the current selected node in the JTree
     * @throws org.mozillatranslator.gui.HasNoTreeException if this frame has no
     * JTree
     */
    public TreePath getTreeSelection() throws HasNoTreeException;

    /**
     * Returns the selected row in the JTable of this MozFrame
     * @return the selected row in the JTable of this MozFrame
     */
    public int getSelectionIndex();

    /**
     * Returns the number of row in the JTable of this MozFrame
     * @return the rowCount() value of the JTable of this MozFrame
     */
    public int getMaxIndex();

    /**
     * Returns the Phrase corresponding to the index in the JTable model
     * @param index the row number in the JTable model
     * @return the Phrase corresponding to the index in the JTable model
     */
    public Phrase getPhraseByIndex(int index);

    /**
     * Fires an event to update table rows from first to last
     * @param first the first table row to mark as changed
     * @param last the last table row to mark as changed
     */
    public void tableRowsChanged(int first, int last);

    /**
     * Fires an event to update the entire table
     */
    public void tableDataChanged();
}
