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

import org.mozillatranslator.io.component.ComponentToXmlAccess;
import org.mozillatranslator.io.common.ImportExportDataObject;
import org.mozillatranslator.io.*;
import java.io.*;
import java.util.Iterator;

/** Represent a Mozilla component
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class Component extends MozTreeNode {
    public static final String TYPE_DIR = "dir";
    public static final String TYPE_FILE = "file";
    public static final String ALTEXPORTDIR_LOCALE_PATTERN = "[:locale:]";
    
    private PartialAccess pa = new PartialAccess();
    private ComponentToXmlAccess xmlAccess = new ComponentToXmlAccess();
    private String exportedToDir = null;
    
    /** Creates new Component
     * @param n the name of the component
     * @param p The parent of this component, usually
     * a Platform or region
     */
    public Component(String n, TreeNode p) {
        super(n, p, TreeNode.LEVEL_COMPONENT);
    }
    
    /**
     * Loads a partial glossary (as a glossary o XML file)
     *
     * @param dataObject An ImportExportDataObject representing the importing
     *                   process
     **/
    public void load(ImportExportDataObject dataObject) throws IOException {
        dataObject.setNode(this);
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_PARTIALGLOSSARY:
                pa.load(dataObject);
                break;
                
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.load(dataObject);
                break;
        }
    }
    
    /**
     * Saves a partial glossary (as a glossary or XML file)
     *
     * @param dataObject An ImportExportDataObject representing the exporting
     *                   process
     **/
    public void save(ImportExportDataObject dataObject) throws IOException {
        dataObject.setNode(this);
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_PARTIALGLOSSARY:
                pa.save(dataObject);
                break;
                
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.save(dataObject);
                break;
        }
    }

    public String getExportedToDir() {
        return exportedToDir;
    }

    public void setExportedToDir(String exportedToDir) {
        this.exportedToDir = exportedToDir;
    }
}
