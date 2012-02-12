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

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.mozillatranslator.batchcontrol.BatchControl;
import org.mozillatranslator.runner.LoadGlossaryDataObject;
import org.mozillatranslator.runner.LoadGlossaryRunner;

/** This is the main class that is run
 * @author Henrik Lynggaard
 * @version 1.0 */
public class Program {
    /** the main method
     * @param args the command line arguments */
    public static void main(String[] args) {
        if (args.length > 0) {
            Kernel.init(false);
            Kernel.appLog.info("Entering batch mode");
            BatchControl bc = new BatchControl();
            bc.run(args);
        } else {
            Kernel.init(true);
            Kernel.appLog.info("Entering GUI mode");
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        Kernel.startWindow();
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            }

            LoadGlossaryDataObject dao = new LoadGlossaryDataObject();
            dao.setFileName(Kernel.settings.getString(Settings.DATAMODEL_FILENAME,
                                                      "Glossary.zip"));
            LoadGlossaryRunner task = new LoadGlossaryRunner();
            task.runTask(dao);
        }
    }
}
