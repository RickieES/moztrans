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
 * Tsahi Asher <tsahi_75@yahoo.com>
 *
 */

package org.mozillatranslator.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * a helper class to set up dialog properties
 * @author Serhiy Brytskyy
 * @version 1.2
 */
public abstract class JDialogHelper {
    /**
     *set action for a dialog when the Esc or Enter keys are pressed - map Esc
     *to Cancel button and Enter to OK button.
     *@param dlg the dialog to set
     *@param btnOK the OK button
     *@param btnCancel the Cancel button
     */
    public static void setupOKCancelHotkeys(JDialog dlg, JButton btnOK, final JButton
            btnCancel) {
        dlg.getRootPane().setDefaultButton(btnOK);
        String actionName = "Close";
        Action closeAction = new AbstractAction(actionName) {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCancel.doClick();
            }
        };

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JLayeredPane layeredPane = dlg.getLayeredPane();
        layeredPane.getActionMap().put(actionName, closeAction);

        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke,
                actionName);
    }
}