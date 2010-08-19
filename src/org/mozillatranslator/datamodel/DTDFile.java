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

import java.util.ArrayList;
import java.util.Iterator;
import org.mozillatranslator.io.file.*;
import org.mozillatranslator.io.common.*;
import org.mozillatranslator.kernel.*;

public class DTDFile extends MozFile {
    private static FileAccess dtdaccess = new DTDAccess();
    private static FileToXmlAccess xmlAccess = new FileToXmlAccess();
    private ArrayList externalEntities;
    
    
    DTDFile(String n, TreeNode p) {
        super(n, p);
    }
    
    public void load(ImportExportDataObject dataObject) {
        super.load(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                dtdaccess.load(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.load(dataObject);
                break;
        }
    }
    
    public void save(ImportExportDataObject dataObject) {
        super.save(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                dtdaccess.save(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.save(dataObject);
                break;
        }
    }
    
    public String getTypeName() {
        return "DTD";
    }
    
    public ArrayList getExternalEntities() {
        return externalEntities;
    }
    
    public void setExternalEntities(ArrayList externalEntities) {
        this.externalEntities = externalEntities;
    }
    
    public void removeChildren() {
        Iterator itChildren = children.iterator();
        while (itChildren.hasNext()) {
            MozTreeNode child = (MozTreeNode) itChildren.next();
            if (child.getAllChildren() != null) {
                child.removeChildren();
            }
            child.setParent(null);
        }
        itChildren = null;

        while (children.size() > 0) {
            children.remove(0);
        }
        
        if (externalEntities != null) {
            while (externalEntities.size() > 0) {
                externalEntities.remove(0);
            }
        }
    }

}
