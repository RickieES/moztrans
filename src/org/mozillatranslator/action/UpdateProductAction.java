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
 * Ricardo Palomares (modifications to merge CVS Import Directory and Update
 *                    Product in a single option)
 */
package org.mozillatranslator.action;

import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.mozillatranslator.datamodel.ClearResultComment;
import org.mozillatranslator.datamodel.Product;
import org.mozillatranslator.datamodel.TreeNode;
import org.mozillatranslator.dataobjects.ProductUpdateDataObject;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.ProductImportExport;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
import org.mozillatranslator.io.common.FileUtils;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.runner.ImportFromCvsRunner;
import org.mozillatranslator.runner.UpdateProductRunner;
import org.mozillatranslator.util.GuiTools;

/**
 * Runs the Update product process which loads the original texts
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class UpdateProductAction extends AbstractAction {

    /**
     * Creates new UpdateProductAction
     */
    public UpdateProductAction() {
        super(Kernel.translate("menu.file.update_product.label"), null);
    }

    /**
     * Called when the action is triggered.
     *
     * @param actionEvent The action event
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Thread runner;

        Product[] prodList;
        ProductImportExport piePanel = new ProductImportExport(ProductImportExport.TYPE_IMPORT_ORIGINAL);
        JDialog pieDialog = new JDialog(Kernel.mainWindow, Kernel.translate("menu.file.update_product.label"), true);
        pieDialog.setContentPane(piePanel);
        pieDialog.pack();
        GuiTools.placeFrameAtCenter(pieDialog);
        pieDialog.setVisible(true);
        ShowWhatDialog swd = new ShowWhatDialog();

        // If the user has pressed OK in main product update dialog, showed him the column selection dialog and he
        // also pressed OK
        if (piePanel.isOkPressed() && swd.showDialog()) {
            // Let's clear QA results
            Kernel.datamodel.traverse(new ClearResultComment(), TreeNode.LEVEL_PHRASE);

            // Initialize the DataObject used to pass the parameters to runners
            ProductUpdateDataObject puDO = new ProductUpdateDataObject();

            puDO.setRunAutoTranslate(piePanel.isExportOnlyModified());

            prodList = piePanel.getSelectedProducts();
            for (Product p : prodList) {
                puDO.setProd(p);
                puDO.setL10n(Kernel.ORIGINAL_L10N);

                // If CVS Import Path is not empty, we'll run CVS Import Directory,
                // else traditional JAR Product Update
                if (p.getCVSImportOriginalPath().trim().length() > 0) {
                    File selectedDir = new File((prodList.length == 1) ? piePanel.getImpExpPath()
                                                                       : FileUtils.getFullRepoDir(p.getCVSImportOriginalPath()));
                    puDO.setImportDir(selectedDir);
                    runner = new ImportFromCvsRunner(puDO);
                } else {
                    puDO.setImportDir(null);
                    runner = new UpdateProductRunner(puDO);
                }
                Kernel.feedback.progress("Updating " + puDO.getProd().getName());
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
            Kernel.feedback.progress("Sorting change list");
            Collections.sort(puDO.getChangeList());

            String localeName = swd.getSelectedLocale();
            if (puDO.isRunAutoTranslate()) {
                Kernel.feedback.progress("Update product: auto-translating");
                Kernel.ts.translatePhraseList(puDO.getChangeList(), localeName);
            }

            List cols = swd.getSelectedColumns();
            Kernel.feedback.progress("Ready");
            new ComplexTableWindow(Kernel.translate("changed_strings"), puDO.getChangeList(), cols, localeName, null);
        }
    }
}
