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
 */


package org.mozillatranslator.io;

import org.mozillatranslator.dataobjects.ImportExportDataObject;
import org.mozillatranslator.io.glossary.PropertiesPersistance;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class PartialAccess {
    
    /** Creates new PartialAccess */
    public PartialAccess() {
    }
    
    public void load(ImportExportDataObject dataObject) {
        PropertiesPersistance pa = new PropertiesPersistance();
        Component currentNode = (Component) dataObject.getNode();
        
        pa.loadPartialGlossary(currentNode, dataObject.getRealFile());
    }
    
    public void save(ImportExportDataObject dataObject) {
        PropertiesPersistance pa = new PropertiesPersistance();
        Component currentNode = (Component) dataObject.getNode();
        
        pa.savePartialGlossary(currentNode, dataObject.getRealFile());
    }
}
