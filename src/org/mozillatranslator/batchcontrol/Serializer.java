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
package org.mozillatranslator.batchcontrol;

import java.util.logging.Level;
import org.mozillatranslator.dataobjects.DataObject;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.runner.MozRunner;

/**
 *
 * @author henrik
 */
public class Serializer {

    public static void action(MozRunner runner, DataObject dao) {
        Thread thread;
        try {
            runner.setDataObject(dao);
            thread = new Thread(runner);
            thread.start();
            thread.join();
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error during bacth run{0}", e);
        }
    }

    public static void action(Thread thread) {
        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error during bacth run{0}", e);
        }
    }
}
