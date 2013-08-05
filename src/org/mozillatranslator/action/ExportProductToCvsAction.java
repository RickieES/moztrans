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

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.mozillatranslator.datamodel.Product;
import org.mozillatranslator.gui.dialog.ProductImportExport;
import org.mozillatranslator.io.common.FileUtils;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.runner.ExportToCvsRunner;
import org.mozillatranslator.util.GuiTools;

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
        Product[] prodList;
        ProductImportExport piePanel = new ProductImportExport(ProductImportExport.TYPE_EXPORT_TRANSLATION);
        JDialog pieDialog = new JDialog(Kernel.mainWindow, Kernel.translate("menu.export.cvs_translation.label"), true);
        pieDialog.setContentPane(piePanel);
        pieDialog.pack();
        GuiTools.placeFrameAtCenter(pieDialog);
        pieDialog.setVisible(true);

        if (piePanel.isOkPressed()) {
            String l10n = JOptionPane.showInputDialog(Kernel.mainWindow, "Select locale to export",
                    Kernel.settings.getString(Settings.STATE_L10N));
            if (l10n != null) {
                Kernel.settings.setString(Settings.GUI_EXPORT_FILE_CHOOSER_PATH, "");
                prodList = piePanel.getSelectedProducts();
                for(Product p : prodList) {
                    try {
                        File selectedDir = new File((prodList.length == 1) ?
                                piePanel.getImpExpPath() :
                                FileUtils.getFullRepoDir(p.getCVSImpExpTranslationPath()));
                        ExportToCvsRunner runner = new ExportToCvsRunner(p, selectedDir, l10n, piePanel.isExportOnlyModified());
                        runner.start();
                        // We wait for the thread to end, since the datamodel is not really designed to be
                        // thread-safe
                        runner.join();
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(Kernel.mainWindow, "Operation interrupted while dealing with product "
                                + p.getName() + ", exitting");
                        return;
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(Kernel.mainWindow, e.getMessage(),
                                "Error in product " + p.getName(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
