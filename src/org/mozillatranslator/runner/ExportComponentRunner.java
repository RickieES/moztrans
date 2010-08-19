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

package org.mozillatranslator.runner;

import org.mozillatranslator.io.common.ImportExportDataObject;
import org.mozillatranslator.io.ComponentTransferDataObject;
import org.mozillatranslator.kernel.*;
import java.io.*;

/** This runner will export a component
 * @author Henrik Lynggaard */
public class ExportComponentRunner extends MozTask {
    /**
     * Creates a new instance of ExportComponentRunner
     */
    public ExportComponentRunner() {
    }

    /** the main method */
    public void  taskImplementation() throws MozException {
        ComponentTransferDataObject dao = (ComponentTransferDataObject) dataObject;
        ImportExportDataObject data = new ImportExportDataObject();
        data.setFormat(dao.getFormat());
        data.setFileName(dao.getFileName());
        data.setNode(dao.getComponent());
        try {
            dao.getComponent().save(data);
        } catch (IOException e) {
            throw new MozException("Error during  export of component", e);
        }
    }

    public String getTitle() {
        return "Export Component";
    }
}
