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

package org.mozillatranslator.kernel;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;

/**
 *
 * @author  Henrik Lynggaard Hansen
 */
public class FeedbackProvider {
    private boolean gui;
    private MozTask currentTask;
    private ShowWhatDialog swd;
    private Thread runner;

    /** Creates a new instance of FeedbackProvider */
    public FeedbackProvider(boolean gui) {
        this.gui = gui;
    }

    /*
     * Moz task related methods
     */
    public void runTask(MozTask task) {
        currentTask = task;

        Kernel.appLog.log(Level.INFO, "Beginning: {0}", currentTask.getTitle());

        if (gui) {
            Kernel.mainWindow.status.setString("Running: " +
                                               currentTask.getTitle());
            Kernel.mainWindow.status.setIndeterminate(true);
            runner = new Thread(task);
            runner.start();
        } else {
            System.out.print(task.getTitle() + "\t\t");
            task.run();
        }
    }

    public void progress(String message) {
        if (gui) {
            Kernel.mainWindow.status.setString(message);
        }
    }
    
    public void join(long millis) throws InterruptedException {
        runner.join(millis);
    }
    
    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
    
    public boolean isAlive() {
        return runner.isAlive();
    }
    
    public void endTask() {
        Kernel.appLog.log(Level.INFO, "Finished: {0}", currentTask.getTitle());
        if (gui) {
            List columns;
            List affected;
            String localeName;
            Kernel.mainWindow.status.setString("Ready");
            Kernel.mainWindow.status.setIndeterminate(false);
            affected = currentTask.getAffectedList();

            // open List window
            if (affected != null && !affected.isEmpty() && swd.showDialog()) {
                localeName = swd.getSelectedLocale();
                columns = swd.getSelectedColumns();

                Collections.sort(affected);
                new ComplexTableWindow(currentTask.getTitle(), affected, columns, localeName, null);
            }
        } else {
            System.out.println("<< Done >>");
            //print List
        }
    }

    public void abortTask(MozException e) {
        Kernel.appLog.log(Level.SEVERE, "Aborting task " + currentTask.getTitle(), e);
        if (gui) {
            Kernel.mainWindow.status.setString("Failed");
            Kernel.mainWindow.status.setIndeterminate(false);
        } else {
            System.out.println("<< Failed >>");
        }
    }
}
