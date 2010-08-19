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
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author rpalomares
 */
public class ClearFuzzy extends AbstractAction {
    private JFrame parentFrame;

    /** Creates a new instance of ClearFuzzy
     * @param parentFrame
     */
    public ClearFuzzy(JFrame parentFrame) {
        super(Kernel.translate("menu.edit.clear_fuzzy.label"));
        putValue(Action.LARGE_ICON_KEY,
                 new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/edit-clear.png")));
        putValue(Action.SHORT_DESCRIPTION,
                Kernel.translate("menu.edit.clear_fuzzy.label"));
        this.parentFrame = parentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int n = JOptionPane.showConfirmDialog(parentFrame,
                "Are you sure you want to clear ALL Fuzzy flags?",
                "Clear All Fuzzy flags", JOptionPane.YES_NO_OPTION);
        
        if (n == 0) {
            Kernel.datamodel.traverse(new ClearFuzzyTraverse(), TreeNode.LEVEL_PHRASE);
        }
    }
    
}
