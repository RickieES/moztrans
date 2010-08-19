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
package org.mozillatranslator.runner;

import org.mozillatranslator.kernel.*;
import org.mozillatranslator.filter.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;
import java.util.*;

/** This runner is used to get a list of redundant strings
 * @author Henrik Lynggaard */
public class ViewRedundantRunner extends Thread {

    /** Creates a new instance of ExportFileRunner */
    public ViewRedundantRunner() {
    }

    /** This is called to get the list */
    @Override public void run() {
        String localeName;
        List collectedList;
        List redundant;
        List all;
        RedundantCollectTraverse rd;

        ShowWhatDialog swd;
        List cols;

        // Show the Which Columns dialog
        swd = new ShowWhatDialog();

        if (swd.showDialog()) {
            localeName = swd.getSelectedLocale();
            cols = swd.getSelectedColumns();
            collectedList = new ArrayList();
            all = new ArrayList();

            // First we take all known phrases to a big list
            rd = new RedundantCollectTraverse();
            Kernel.datamodel.traverse(rd, TreeNode.LEVEL_FILE);
            all = rd.getResultList();

            Collections.sort(all, Phrase.TEXT_ORDER);
            // Check for redundant original texts
            Iterator<Phrase> phraseIterator = all.iterator();
            Phrase previous = null;
            redundant = new ArrayList();
            while (phraseIterator.hasNext()) {
                Phrase curPhrase = phraseIterator.next();
                
                if ((previous == null) ||
                        (curPhrase.getText().equalsIgnoreCase(previous.getText()))) {
                    redundant.add(curPhrase);
                } else {
                    if (redundant.size() > 1) {
                        collectedList.addAll(redundant);
                    }
                    redundant.clear();
                    redundant.add(curPhrase);
                }
                previous = curPhrase;
            }
            
            if (redundant.size() > 1) {
                collectedList.addAll(redundant);
            }

            // Make sure to decrease references on all files not in the collected list
            Kernel.datamodel.traverse(new RedundantPostOpTraverse(collectedList),
                                      TreeNode.LEVEL_FILE);

            // Display the phrases
            new ComplexTableWindow(Kernel.translate("more_than_once"),
                                   collectedList, cols, localeName,
                                   new RedundantViewToolbar());
        }
    }
}
