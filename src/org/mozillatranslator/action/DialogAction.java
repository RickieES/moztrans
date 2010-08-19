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
package org.mozillatranslator.action;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import org.mozillatranslator.datamodel.ClearResultComment;
import org.mozillatranslator.datamodel.TreeNode;
import org.mozillatranslator.filter.Filter;
import org.mozillatranslator.filter.FilterRunner;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
import org.mozillatranslator.kernel.Kernel;

/** This actions displays a about mozillatranslator
 * dialog box.
 *
 * @author Henrik
 * @version 4.15
 */
public abstract class DialogAction extends AbstractAction {
    private String title;
    /**
     * Method to implement by all clases extending DialogAction
     * @param localeName Locale code to check
     * @return a Filter implementation to perform checks on strings
     */
    protected abstract Filter getFilter(String localeName);
    
    /**
     * Constructor for DialogAction
     * @param str The title of the action
     */
    public DialogAction(String str) {
        super(str, null);
        title = str;
    }
    
    public void actionPerformed(ActionEvent evt) {
        List collectedList;
        String localeName;
        ShowWhatDialog swd;
        List cols;
        
        Kernel.datamodel.traverse(new ClearResultComment(), TreeNode.LEVEL_PHRASE);
        swd = new ShowWhatDialog();
        if (swd.showDialog()) {
            localeName = swd.getSelectedLocale();
            cols = swd.getSelectedColumns();
            Filter filter = getFilter(localeName);
            collectedList  = FilterRunner.filterDatamodel(filter);
            Collections.sort(collectedList);
            new ComplexTableWindow(title, collectedList, cols, localeName, null);
        }
    }
}