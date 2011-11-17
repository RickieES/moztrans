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

import java.util.*;
import java.io.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.io.ImportTranslationDataObject;

/** This runner will import a translation from a chrome
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class ImportTranslationRunner extends MozTask {
    /** Creates new ImportTranslationRunner */
    public ImportTranslationRunner() {
    }

    /** The main method
     */
    @Override
    public void taskImplementation() throws MozException {
        ImportTranslationDataObject dao = (ImportTranslationDataObject) dataObject;
        Kernel.startTimeBatch();
        ProductChild pc = (ProductChild) dao.getNode();
        ProductChildInputOutputDataObject data = new ProductChildInputOutputDataObject();
        data.setL10n(dao.getLocalization());
        data.setAuthor("");
        data.setBuffInStream(null);
        data.setBuffOutStream(null);
        data.setChangeList(new ArrayList(500));
        data.setDisplay("");
        data.setJarInStream(null);
        data.setJarOutStream(null);
        data.setFormat(ProductChildInputOutputDataObject.FORMAT_JAR);
        data.setPreviewUrl("");
        data.setVersion("");
        data.setRealFile(new File(dao.getFileName()));
        try {
            pc.load(data);
        } catch (IOException e) {
            throw new MozException("Error during import translation", e);
        }
        Kernel.endTimeBatch();
    }

    @Override
    public String getTitle() {
        return "Import translation";
    }
}
