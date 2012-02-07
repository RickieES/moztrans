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
 * Ricardo Palomares (refactoring to remove subcomponent from the datamodel)
 *
 */

package org.mozillatranslator.io.glossary;

import java.io.*;
import java.util.logging.Level;
import org.mozillatranslator.datamodel.DataModel;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.runner.LoadGlossaryDataObject;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class SerializedPersistance implements GlossaryAccess {

    /** Creates new SerializedPersistance */
    public SerializedPersistance() {
    }

    @Override
    public void saveEntireGlossary() {
        String fileName;
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fileName = Kernel.settings.getString(Settings.DATAMODEL_FILENAME, "glossary.zip");

            fos = new FileOutputStream(fileName, false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(Kernel.datamodel);
            oos.close();
        } catch (Exception e) {
            // TODO(24/2/2002): refactor exception handling
            Kernel.appLog.log(Level.SEVERE, "Unable to save glossary", e);
        }
    }


    @Override
    public void loadEntireGlossary(LoadGlossaryDataObject data) {
        FileInputStream fis;
        ObjectInputStream ois;
        File testing;
        try {
            testing = data.getRealFile();
            if (testing.exists()) {
                fis = new FileInputStream(data.getFileName());
                ois = new ObjectInputStream(fis);
                Kernel.datamodel = (DataModel) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            // TODO(24/2/2002): refactor exception handling
            Kernel.appLog.log(Level.SEVERE, "Unable to load glossary", e);
        }
    }
}
