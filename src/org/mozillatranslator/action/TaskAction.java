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

import java.awt.event.*;
import javax.swing.*;
import java.util.logging.Level;

import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author  henrik
 */
public class TaskAction extends AbstractAction {
    /** Dialog title */
    protected String title;
    /** Task to be executed through the action invocation */
    protected MozTask task;
    /** Dialog to be shown through the action invocation */
    protected MozDialog dialog;

    /** Creates a new instance of TaskAction
     * @param title The dialog title
     * @param dialog The dialog object to be shown
     * @param task The task to execute
     */
    public TaskAction(String title, MozDialog dialog, MozTask task) {
        super(title, null);
        this.dialog = dialog;
        this.task = task;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Kernel.appLog.log(Level.INFO, "Showing action dialog {0}", title);

        if (dialog.showDialog()) {
            task.runTask(dialog.getDataObject());
        }
    }
}
