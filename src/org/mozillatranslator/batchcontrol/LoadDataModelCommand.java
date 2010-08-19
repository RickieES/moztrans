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

import org.mozillatranslator.kernel.*;
import org.mozillatranslator.runner.*;
import org.w3c.dom.*;

/**
 *
 * @author  Henrik Lynggaard
 */
public class LoadDataModelCommand  extends BatchCommand {
    /** Creates a new instance of LoadDataModelCommand */
    public LoadDataModelCommand() {
    }

    public boolean action(Element command) throws BatchException {
        Kernel.appLog.info("Batch: loading glossary");

        try {
            String filename = getSimpleParameter(command, "filename");
            LoadGlossaryDataObject dao = new LoadGlossaryDataObject();
            dao.setFileName(filename);
            LoadGlossaryRunner task = new LoadGlossaryRunner();
            task.runTask(dao);
        } catch (Exception e) {
            throw new BatchException("Failure to load glossary", e);
        }
        return true;
    }
}
