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
package org.mozillatranslator.action;

import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import org.mozillatranslator.runner.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author Henrik Lynggaard
 * @version 1.00
 */
public class ImportProductTranslationFromCvsAction extends AbstractAction {

    /** Creates new AboutAction */
    public ImportProductTranslationFromCvsAction() {
        super(Kernel.translate("menu.import.translation.cvs.label"), null);
    }

    /** This gets called when the event happens
     * @param evt the event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        Product prod;
        File selectedDir;
        UpdateProduct up;
        String l10n;
        ImportFromCvsRunner runner;

        // Bring a dialog box
        up = new UpdateProduct("Import from SCM", UpdateProduct.TYPE_IMPORT_TRANSLATION);
        prod = up.showDialog();

        try {
            selectedDir = new File(up.getCVSImportPath());
        } catch (java.lang.NullPointerException e) {
            selectedDir = null;
        }

        if ((prod != null) && (selectedDir != null)) {
            l10n = JOptionPane.showInputDialog(Kernel.mainWindow, "Select locale to import",
                    Kernel.settings.getString(Settings.STATE_L10N));
            if (l10n != null) {
                runner = new ImportFromCvsRunner(prod, selectedDir, l10n);
                runner.start();
            }
        }
    }
}
