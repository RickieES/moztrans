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
 * Ricardo Palomares (make CvsTransfer.java a non-static class)
 *
 */

package org.mozillatranslator.runner;

import java.util.List;
import org.mozillatranslator.datamodel.GenericFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.dataobjects.ProductUpdateDataObject;
import org.mozillatranslator.io.common.CvsTransfer;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 * This runner will update a product
 *
 * @author Henrik Lynggaard
 * @version 1.0
 **/
public class ImportFromCvsRunner extends Thread {
    /**
     * The data object containing all information needed to process a product update
     */
    private ProductUpdateDataObject puDO;

    /**
     * Creates new ImporFromCvsRunner
     *
     * @param puDO a ProductUpdateDataObject containing all relevant data for an ImportFromCvsRunner object creation
     */
    public ImportFromCvsRunner(ProductUpdateDataObject puDO) {
        this.puDO = puDO;
    }
    
    /**
     * This is the main method
     **/
    @Override
    public void run() {
        CvsTransfer cvsInstance = new CvsTransfer(this.puDO.getProd(), this.puDO.getImportDir());
        
        if (this.puDO.getL10n().equals(Kernel.ORIGINAL_L10N)) {
            List<Phrase> changed = cvsInstance.loadProduct();
            if (changed.size() > 0) {
                for(Phrase curPhrase : changed) {
                    curPhrase.setFuzzy(true);
                }
                this.puDO.getChangeList().addAll(changed);
                
                for(Phrase curPhrase : changed) {
                    if (curPhrase.getName().indexOf("lang.version") > -1) {
                        Kernel.settings.setString(Settings.STATE_VERSION,
                        curPhrase.getText());
                    }

                    GenericFile mfile = (GenericFile) curPhrase.getParent();
                    if (mfile != null) {
                        mfile.decreaseReferenceCount();
                    }
                }
            }
            
            // FIXME maybe there is better way to handle versions
            this.puDO.getProd().setVersion(Kernel.settings.getString(Settings.STATE_VERSION));
        } else {
            cvsInstance.loadTranslation(this.puDO.getL10n());
        }
    }
}
