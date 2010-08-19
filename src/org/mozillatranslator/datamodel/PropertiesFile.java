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

import org.mozillatranslator.io.file.PropertiesAccess;
import org.mozillatranslator.io.file.FileToXmlAccess;
import org.mozillatranslator.io.file.FileAccess;
import org.mozillatranslator.io.common.*;

/**
 * This class deals with Mozilla Properties files. "Mozilla Properties" are
 * a variant of Sun's Java Properties files that uses UTF-8 direct encoding
 * instead of Unicode escape sequences
 * @author rpalomares
 */
public class PropertiesFile extends MozFile {
    private static FileAccess propertiesaccess = new PropertiesAccess();
    private static FileToXmlAccess xmlAccess = new FileToXmlAccess();
    
    PropertiesFile(String n, TreeNode p) {
        super(n, p);
    }
    
    /**
     * Loads a Properties file
     * @param dataObject an ImportExportDataObject that holds both a reference
     * to a Properties file and a array list of key/value pairs
     */
    @Override
    public void load(ImportExportDataObject dataObject) {
        super.load(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                propertiesaccess.load(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.load(dataObject);
                break;
        }
    }
    
    /**
     * Saves a Properties file
     * @param dataObject an ImportExportDataObject that holds both a reference
     * to a Properties file and a array list of key/value pairs
     */
    @Override
    public void save(ImportExportDataObject dataObject) {
        super.save(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                propertiesaccess.save(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.save(dataObject);
                break;
        }
    }
    
    /**
     * Returns a human-readable definition of this kind of file
     * @return a human-readable definition of this kind of file
     */
    @Override
    public String getTypeName() {
        return "Properties";
    }
}
