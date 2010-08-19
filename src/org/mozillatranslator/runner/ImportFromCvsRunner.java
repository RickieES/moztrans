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

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.*;
import org.mozillatranslator.io.common.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;

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
     * Creates new UpdateProductRunner
     *
     * @param p     the product to update
     * @param id    the dir from where to import
     * @param l10n  the l10n to import
     **/
    public ImportFromCvsRunner(Product p, File id, String l10n) {
        this.prod = p;
        this.importDir = id;
        this.l10n = l10n;
    }
    

    /**
     * This is the main method
     **/
    public void run() {
        CvsTransfer cvsInstance = new CvsTransfer(this.prod, this.importDir);
        
        if (l10n.equals(Kernel.ORIGINAL_L10N)) {
            List changed = cvsInstance.loadProduct();
            if (changed.size() > 0) {
                Iterator modelIterator = changed.iterator();
                while (modelIterator.hasNext()) {
                    Phrase curPhrase = (Phrase) modelIterator.next();
                    curPhrase.setFuzzy(true);
                }
                
                ShowWhatDialog swd = new ShowWhatDialog();
                if (swd.showDialog()) {
                    String localeName = swd.getSelectedLocale();
                    List cols = swd.getSelectedColumns();
                    
                    Collections.sort(changed);
                    new ComplexTableWindow(Kernel.translate("changed_strings"),
                            changed, cols, localeName, null);
                } else {
                    modelIterator = changed.iterator();
                    while (modelIterator.hasNext()) {
                        Phrase curPhrase = (Phrase) modelIterator.next();
                        
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
