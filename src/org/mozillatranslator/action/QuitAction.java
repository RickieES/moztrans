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

import java.util.logging.*;
import javax.swing.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.runner.*;

/** This exits the program after a user confimation and possibly a
 * save of the glossary and settings
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class QuitAction extends AbstractAction {

    /** Creates new QuitAction
     */
    public QuitAction() {
        super(Kernel.translate("menu.file.quit.label"), null);
    }

    /** is called when the action is triggered
     * @param actionEvent The action event
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        doExit();
    }

    /** The actual quit rutine, that asks for user confimation
     */
    public void doExit() {
        int result;

        result = JOptionPane.showConfirmDialog(Kernel.mainWindow,
                Kernel.translate("save_glossary_quit"), Kernel.translate("Exit"),
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        if (result == JOptionPane.YES_OPTION) {
            SaveGlossaryRunner task = new SaveGlossaryRunner();
            task.runTask(null);
            Kernel.settings.save();
            while (Kernel.feedback.isAlive()) {
                try {
                    Kernel.feedback.join(200);
                    Kernel.feedback.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(QuitAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.exit(0);
        }
    }
}
