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

import org.mozillatranslator.io.FileTransferDialogDataObject;
import org.mozillatranslator.io.common.*;
import org.mozillatranslator.kernel.*;
import java.io.*;

/** This runner will export a file
 * @author Henrik Lynggaard */
public class ExportFileRunner extends MozTask {
    /** Creates a new instance of ExportFileRunner */
    public ExportFileRunner() {
    }

    /** the main method
     * @throws MozException
     */
    @Override
    public void taskImplementation() throws MozException {
        FileTransferDialogDataObject dao = (FileTransferDialogDataObject) dataObject;
        ImportExportDataObject data = new ImportExportDataObject();

        data.setFileName(dao.getFileName());
        data.setChangeList(null);
        data.setFormat(dao.getFormat());
        data.setL10n(dao.getLocalization());
        data.setFileContent(null);
        dao.getChosenFile().increaseReferenceCount();
        dao.getChosenFile().save(data);
        dao.getChosenFile().decreaseReferenceCount();
        try {
            FileUtils.saveWithLicence(dao.getFileName(), data.getFileContent(),dao.getChosenFile());
        } catch (IOException e) {
            throw new MozException("Error during file export", e);
        }
    }

    /**
     * Returns a title for the task being executed
     * @return title for the task being executed
     */
    @Override
    public String getTitle() {
        return "Export file";
    }
}
