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

package org.mozillatranslator.runner;

import java.lang.reflect.InvocationTargetException;
import org.mozillatranslator.io.glossary.GlossaryAccess;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.MozException;
import org.mozillatranslator.kernel.MozTask;
import org.mozillatranslator.kernel.Settings;

/** This saves the glossary to disc
 *
 * @author Henrik Lynggaard
 * @version 1.2
 */
public class SaveGlossaryRunner extends MozTask {
    /** Creates new SaveGlossary */
    public SaveGlossaryRunner() {
    }
    
    /**
     * Returns a title for the task being executed
     * @return title for the task being executed
     */
    @Override
    public String getTitle() {
        return "Save glossary";
    }

    /**
     * The main method
     *
     * @throws MozException
     */
    @Override
    public void taskImplementation() throws MozException {
        String pclass;
        GlossaryAccess ga;
        try {
            pclass = Kernel.settings.getString(Settings.DATAMODEL_PCLASS,
                    "org.mozillatranslator.io.PropertiesPersistance");
            ga = (GlossaryAccess) Class.forName(pclass)
                    .getDeclaredConstructor().newInstance();
            ga.saveEntireGlossary();
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | NoSuchMethodException
                | SecurityException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new MozException("Error while saving glossary ", e);
        }
    }
}
