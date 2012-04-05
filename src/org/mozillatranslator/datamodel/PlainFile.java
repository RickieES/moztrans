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

package org.mozillatranslator.datamodel;

import org.mozillatranslator.dataobjects.ImportExportDataObject;
import org.mozillatranslator.io.file.TextAccess;
import org.mozillatranslator.io.file.FileToXmlAccess;
import org.mozillatranslator.io.file.FileAccess;
import org.mozillatranslator.io.common.*;

public class PlainFile extends MozFile {
    private static FileAccess textaccess = new TextAccess();
    private static FileToXmlAccess xmlAccess = new FileToXmlAccess();
    
    PlainFile(String n, TreeNode p) {
        super(n, p);
    }
    
    @Override
    public void load(ImportExportDataObject dataObject) {
        super.load(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                textaccess.load(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.load(dataObject);
                break;
        }
    }
    
    @Override
    public void save(ImportExportDataObject dataObject) {
        super.save(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                textaccess.save(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.save(dataObject);
                break;
        }
    }
    
    @Override
    public String getTypeName() {
        return "Plain text";
    }
}
