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

import org.mozillatranslator.dataobjects.MigrateProductDataObject;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.gui.*;
/**
 *
 * @author  Henrik Lynggaard
 */
public class MigrateProductRunner extends MozTask {

    /** Creates a new instance of ExportFileRunner */
    public MigrateProductRunner() {
    }
    
    @Override
    public void taskImplementation() throws MozException {
        MigrateProductDataObject dao = (MigrateProductDataObject) dataObject;
        Product productSource, productDestination;
        String l10n;
        
        l10n = dao.getL10n();
        productSource = dao.getSource();
        productDestination = dao.getDestination();
        
        MigrateProductTraverse command = new MigrateProductTraverse(productDestination, l10n);
        productSource.traverse(command, TreeNode.LEVEL_PHRASE);
    }    
    
    @Override
    public String getTitle() {
        return "Migrate product";
    }
}
