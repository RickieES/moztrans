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

package org.mozillatranslator.action;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import org.mozillatranslator.datamodel.ClearResultComment;
import org.mozillatranslator.datamodel.TreeNode;
import org.mozillatranslator.filter.FilterRunner;
import org.mozillatranslator.filter.ReplaceFilter;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.ReplaceDialog;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
import org.mozillatranslator.kernel.Kernel;

/**
 * Replace a string with other
 *
 * @author rpalomares
 */
public class ReplaceAction extends AbstractAction {
    
    /** Creates a new instance of Replace */
    public ReplaceAction() {
        super(Kernel.translate("menu.edit.replace.label"), null);
        Kernel.datamodel.traverse(new ClearResultComment(), TreeNode.LEVEL_PHRASE);
    }

    public void actionPerformed(ActionEvent e) {
        List collectedList;

        ReplaceDialog rd = new ReplaceDialog();

        if (rd.showDialog()) {
            ReplaceFilter replaceFlt = (ReplaceFilter) rd.getDataObject();

            ShowWhatDialog swd = new ShowWhatDialog();
            swd.disableLocaleField();
            if (swd.showDialog()) {
                String localeName = replaceFlt.getLocaleName();
                List cols = swd.getSelectedColumns();
                collectedList  = FilterRunner.filterDatamodel(replaceFlt);
                Collections.sort(collectedList);
                new ComplexTableWindow("Replaced Strings", collectedList, cols,
                        localeName, null);
            }
        }
    }
    
}
