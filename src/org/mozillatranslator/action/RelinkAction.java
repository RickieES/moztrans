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
import org.mozillatranslator.filter.RelinkFilter;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.RelinkDialog;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
import org.mozillatranslator.kernel.Kernel;


/**
 * Walks trhough the whole tree redoing associations between labels, commandkeys
 * and accesskeys
 *
 * @author rpalomares
 */
public class RelinkAction extends AbstractAction {
    
    /** Creates a new instance of RelinkAction */
    public RelinkAction() {
        super(Kernel.translate("menu.edit.relink_strings.label"), null);
        Kernel.datamodel.traverse(new ClearResultComment(), TreeNode.LEVEL_PHRASE);
    }

    public void actionPerformed(ActionEvent e) {
        List collectedList;

        RelinkDialog rd = new RelinkDialog();

        if (rd.showDialog()) {
            RelinkFilter relinkFlt = (RelinkFilter) rd.getDataObject();

            ShowWhatDialog swd = new ShowWhatDialog();
            swd.disableLocaleField();
            if (swd.showDialog()) {
                String localeName = relinkFlt.getLocaleName();
                List cols = swd.getSelectedColumns();
                collectedList  = FilterRunner.filterDatamodel(relinkFlt);
                Collections.sort(collectedList);
                new ComplexTableWindow("Relinked Strings", collectedList, cols,
                        localeName, null);
            }
        }
    }
    
}
