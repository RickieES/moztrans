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
public class ExportProductToCvsAction extends AbstractAction {

    /** Creates new AboutAction */
    public ExportProductToCvsAction() {
        super(Kernel.translate("menu.export.cvs_translation.label"), null);
    }

    /** this gets called when the event happends
     * @param evt the event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        Product prod;
        // bring a dialog box
        UpdateProduct up = new UpdateProduct("Export to SCM",
                UpdateProduct.TYPE_EXPORT_TRANSLATION);
        prod = up.showDialog();

        if (prod != null) {
            String l10n = JOptionPane.showInputDialog(Kernel.mainWindow,
                    "Select locale to export",
                    Kernel.settings.getString(Settings.STATE_L10N));
            if (l10n != null) {
                Kernel.settings.setString(Settings.GUI_EXPORT_FILE_CHOOSER_PATH, "");
                File selectedDir = new File(up.getCVSImportPath());
                ExportToCvsRunner runner = new ExportToCvsRunner(prod, selectedDir,
                        l10n, up.exportOnlyModified());
                runner.start();
            }
        }


    }
}
