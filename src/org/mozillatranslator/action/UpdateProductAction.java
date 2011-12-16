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
import javax.swing.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.runner.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.gui.dialog.*;

/** Runs the Update product process which loads the original
 * texts
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class UpdateProductAction extends AbstractAction {

    /** Creates new UpdateProductAction
     */
    public UpdateProductAction() {
        super(Kernel.translate("menu.file.update_product.label"), null);
    }

    /** Called when the action is triggered.
     * @param actionEvent The action event
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Product prod;
        UpdateProductRunner upr;

        // Bring a dialog box
        UpdateProduct up = new UpdateProduct("Update product",
                UpdateProduct.TYPE_IMPORT_ORIGINAL);
        prod = up.showDialog();

        if (prod != null) {
            // Let's clear QA results
            Kernel.datamodel.traverse(new ClearResultComment(),
                    TreeNode.LEVEL_PHRASE);

            // If CVS Import Path is not empty, we'll run CVS Import Directory,
            // else traditional JAR Product Update
            if (up.getCVSImportPath().trim().length() > 0) {
                File selectedDir = new File(up.getCVSImportPath());
                ImportFromCvsRunner runner = new ImportFromCvsRunner(prod,
                        selectedDir, Kernel.ORIGINAL_L10N, up.isRunAutoTranslate());
                runner.start();
            } else {
                upr = new UpdateProductRunner(prod);
                upr.start();
            }
        }
    }
}
