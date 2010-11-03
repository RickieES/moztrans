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
 * Ricardo Palomares (avoid at least to throw an exception when exporting a JAR
 *                    with custom files)
 */

package org.mozillatranslator.io;

import org.mozillatranslator.io.common.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class CustomContainerAccess extends StructureAccess {
    /** Creates new CustomContainerAccess */
    private String currentLocale;
    private CustomContainer ccon;
    private List cl = new ArrayList();

    public CustomContainerAccess() {
    }

    @Override
    public void save(ProductChildInputOutputDataObject dataObject) throws IOException {
        JarOutputStream jos;
        FileOutputStream fos;
        CustomContainer currentContainer;
        Iterator childIterator;

        if (dataObject.getJarOutStream() == null) {
            // We create the JAR output stream ourselves from the real file
            fos = new FileOutputStream(dataObject.getRealFile());
            jos = new JarOutputStream(fos);
        } else {
            // We use the stream provided by dataObject
            jos = dataObject.getJarOutStream();
        }

        currentContainer = (CustomContainer) dataObject.getProductChild();
        childIterator = currentContainer.iterator();

        while (childIterator.hasNext()) {
            GenericFile childFile = (GenericFile) childIterator.next();

            if (!childFile.isDontExport()) {
                try {
                    childFile.increaseReferenceCount();
                    ImportExportDataObject dao = new ImportExportDataObject();
                    dao.setChangeList(null);
                    dao.setFormat(ImportExportDataObject.FORMAT_MOZILLA);
                    dao.setL10n(dataObject.getL10n());
                    dao.setFileContent(null);
                    childFile.save(dao);
                    childFile.decreaseReferenceCount();

                    JarEntry en = new JarEntry(childFile.getRelativeFilename());
                    jos.putNextEntry(en);
                    FileUtils.saveFile(jos, dao.getFileContent());
                } catch (Exception e) {
                    Kernel.appLog.log(Level.SEVERE,
                            "Unable to write custom file", e);
                    throw new IOException("Unable to write custom file \n"
                            + e.getMessage());
                }
            }
        }
    }

    @Override
    public void load(ProductChildInputOutputDataObject dataObject) throws IOException {
        Iterator containerIterator;

        containerIterator = ccon.iterator();
        try {
            while (containerIterator.hasNext()) {
                ImportExportDataObject dao = new ImportExportDataObject();
                GenericFile cfile = (GenericFile) containerIterator.next();

                FileInputStream fis = new FileInputStream(cfile.getRealFilename());
                dao.setFileContent(FileUtils.loadFile(fis));
                fis.close();
                cfile.increaseReferenceCount();
                dao.setChangeList(cl);
                dao.setFormat(ImportExportDataObject.FORMAT_MOZILLA);
                dao.setL10n(currentLocale);
                cfile.load(dao);
                cfile.decreaseReferenceCount();
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error loading custom file", e);
            throw new IOException("Error loading custom file \n" + e.getMessage());
        }
    }
}
