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
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.mozillatranslator.datamodel.Product;
import org.mozillatranslator.dataobjects.ProductUpdateDataObject;
import org.mozillatranslator.gui.dialog.ProductImportExport;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.runner.ImportFromCvsRunner;
import org.mozillatranslator.util.GuiTools;

/**
 * Imports an existing translation in the form of a set of files into a product in the datamodel
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
        Product[] prodList;
        ProductImportExport piePanel = new ProductImportExport(ProductImportExport.TYPE_IMPORT_TRANSLATION);
        JDialog pieDialog = new JDialog(Kernel.mainWindow, Kernel.translate("menu.import.translation.cvs.label"), true);
        pieDialog.setContentPane(piePanel);
        pieDialog.pack();
        GuiTools.placeFrameAtCenter(pieDialog);
        pieDialog.setVisible(true);
        ShowWhatDialog swd = new ShowWhatDialog();

        if (piePanel.isOkPressed() && swd.showDialog()) {
            // Initialize the DataObject used to pass the parameters to runners
            ProductUpdateDataObject puDO = new ProductUpdateDataObject();

            puDO.setRunAutoTranslate(piePanel.isExportOnlyModified());
            Kernel.settings.setString(Settings.GUI_IMPORT_FILE_CHOOSER_PATH, "");
            prodList = piePanel.getSelectedProducts();
            for(Product p : prodList) {
                puDO.setProd(p);
                puDO.setL10n(swd.getSelectedLocale());

                if (p.getCVSImportOriginalPath().trim().length() > 0) {
                    File selectedDir = new File((prodList.length == 1) ? piePanel.getImpExpPath()
                                                                       : p.getCVSImportOriginalPath());
                    puDO.setImportDir(selectedDir);
                    ImportFromCvsRunner runner = new ImportFromCvsRunner(puDO);
                    runner.start();
                    try {
                        // We wait for the thread to end, since the datamodel is not really designed to be
                        // thread-safe
                        runner.join();
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(Kernel.mainWindow,
                                "Operation interrupted while dealing with product " + p.getName() + ", exitting");
                        return;
                    }
                }
            }
        }
    }
}
