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

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.mozillatranslator.datamodel.GenericFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Product;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
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
     * The product to update
     **/
    private Product prod;
    
    /**
     * The path from where to import
     */
    private File importDir;
    
    /**
     * The l10n we're going to deal with (it may be both the original content or
     * a translation locale code)
     **/
    private String l10n;

    /**
     * Flag indicating wether to run Auto-translate after importing strings
     */
    private boolean runAutoTranslate;

    /**
     * Creates new ImportFromCvsRunner
     *
     * @param p     the product to update
     * @param id    the dir from where to import
     * @param l10n  the l10n to import
     * @param runAutoTranslate true if the user wants to run Auto-Translate on
     *                         news/modified strings
     **/
    public ImportFromCvsRunner(Product p, File id, String l10n, boolean runAutoTranslate) {
        this.prod = p;
        this.importDir = id;
        this.l10n = l10n;
        this.runAutoTranslate = runAutoTranslate;
    }
    

    /**
     * This is the main method
     **/
    @Override
    public void run() {
        CvsTransfer cvsInstance = new CvsTransfer(this.prod, this.importDir);
        
        if (l10n.equals(Kernel.ORIGINAL_L10N)) {
            List<Phrase> changed = cvsInstance.loadProduct();
            if (changed.size() > 0) {
                for(Phrase curPhrase : changed) {
                    curPhrase.setFuzzy(true);
                }
                
                ShowWhatDialog swd = new ShowWhatDialog();
                if (swd.showDialog()) {
                    String localeName = swd.getSelectedLocale();
                    List cols = swd.getSelectedColumns();
                    
                    Collections.sort(changed);

                    if (this.runAutoTranslate) {
                        Kernel.ts.translatePhraseList(changed, localeName);
                    }

                    new ComplexTableWindow(Kernel.translate("changed_strings"),
                            changed, cols, localeName, null);
                } else {
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
            }
            
            // FIXME maybe there is better way to handle versions
            prod.setVersion(Kernel.settings.getString(Settings.STATE_VERSION));
        } else {
            cvsInstance.loadTranslation(this.l10n);
        }
    }
}
