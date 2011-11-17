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
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import org.mozillatranslator.datamodel.Product;
import org.mozillatranslator.datamodel.ResetLastModifiedTimeTraverse;
import org.mozillatranslator.datamodel.TreeNode;
import org.mozillatranslator.gui.dialog.ResetLastModifiedTimePanel;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.util.GuiTools;

/**
 * Action for Advanced -> Reset Last Modified Time
 * @author rpalomares
 */
public class ResetLastModifiedTimeAction extends AbstractAction {

    public ResetLastModifiedTimeAction() {
        super(Kernel.translate("menu.advanced.reset_last_modified_date.label"), null);
        Kernel.datamodel.traverse(new ResetLastModifiedTimeTraverse(), TreeNode.LEVEL_PHRASE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog rlmtDialog = new JDialog(Kernel.mainWindow,
                                         Kernel.translate("menu.advanced.reset_last_modified_date.label"),
                                         true);
        ResetLastModifiedTimePanel rlmtPanel = new ResetLastModifiedTimePanel();
        rlmtDialog.setContentPane(rlmtPanel);
        rlmtDialog.pack();
        GuiTools.placeFrameAtCenter(rlmtDialog);
        rlmtDialog.setVisible(true);

        if (rlmtPanel.isOkPressed()) {
            Long date = rlmtPanel.getSelectedTime();
            Product prod = rlmtPanel.getSelectedProduct();
            prod.traverse(new ResetLastModifiedTimeTraverse(date), TreeNode.LEVEL_TRANSLATION);
        }
    }
}
