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

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;

/** This runner will update a product
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class UpdateProductRunner extends Thread {
    /** the product to update
     */
    private Product prod;

    /** Creates new UpdateProductRunner
     * @param p The product to update
     */
    public UpdateProductRunner(Product p) {
        prod = p;
    }

    /** This is the main method
     */
    @Override
    public void run() {
        List<Phrase> changeList = new ArrayList();
        Kernel.startTimeBatch();
        Iterator productChildIterator = prod.iterator();
        ProductChildInputOutputDataObject dao = new
                ProductChildInputOutputDataObject();

        dao.setL10n(Kernel.ORIGINAL_L10N);
        dao.setAuthor("");
        dao.setBuffInStream(null);
        dao.setBuffOutStream(null);
        dao.setChangeList(changeList);
        dao.setDisplay("");
        dao.setJarInStream(null);
        dao.setJarOutStream(null);
        dao.setFormat(ProductChildInputOutputDataObject.FORMAT_JAR);
        dao.setPreviewUrl("");
        dao.setVersion("");
        try {
            while (productChildIterator.hasNext()) {
                ProductChild pc = (ProductChild) productChildIterator.next();
                dao.setFileName(pc.getJarFile());

                if ((dao.getFileName() != null) && (!pc.getJarFile().equals(""))) {
                    pc.traverse(new ClearMarkTraverse(), TreeNode.LEVEL_TRANSLATION);
                    // dao.setRealFile(new File(pc.getJarFile()));
                    pc.setMark();
                    pc.load(dao);
                    pc.deleteUntouched();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Kernel.mainWindow, "" + e.getMessage(),
                    "Error during update product", JOptionPane.ERROR_MESSAGE);
        }
        Kernel.endTimeBatch();

        if (changeList.size() > 0) {
            for(Phrase curPhrase : changeList) {
                curPhrase.setFuzzy(true);
            }
            ShowWhatDialog swd = new ShowWhatDialog();

            if (swd.showDialog()) {
                String localeName = swd.getSelectedLocale();
                List cols = swd.getSelectedColumns();

                Collections.sort(changeList);
                new ComplexTableWindow(Kernel.translate("changed_strings"),
                                        changeList, cols, localeName, null);
            } else {
                for(Phrase curPhrase : changeList) {
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
        this.prod.setVersion(Kernel.settings.getString(Settings.STATE_VERSION));
    }
}
