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

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import org.mozillatranslator.gui.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;

/**
 * This action open the Chrome view
 * @author Serhiy Brytskyy
 * @version 1.0
 */
public class SearchChromeViewAction extends AbstractAction {

    /** Creates new ChromeViewAction
     */
    public SearchChromeViewAction() {
        super(Kernel.translate("Search_Chrome_view"), null);
        putValue(Action.LARGE_ICON_KEY,
                 new ImageIcon(getClass().getResource("/org/mozillatranslator/resource/show-in-chrome.png")));
        putValue(Action.SHORT_DESCRIPTION,
                Kernel.translate("Search_Chrome_view"));
    }

    /** Is called then the action is triggered
     * @param actionEvent The action event
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        List cols = null;
        String lname = "";
        boolean result = false;
        MainWindow mw;
        MozFrame mf;
        Phrase ph;
        String name;
        JTable table;
        //int selectedIndex;
        //int maxIndex;

        mw = Kernel.mainWindow;
        mf = mw.getInnerFrame();

        if (mf != null) {

            ph = mf.getSelectedPhrase();
            if (ph != null) {
                //selectedIndex = mf.getSelectionIndex();
                //maxIndex = mf.getMaxIndex();

                name = mf.getLocalization();
                table = mf.getTable();

                table.editingCanceled(new ChangeEvent(this));

                //Kernel.editPhrase.showDialog(ph,name,selectedIndex,maxIndex,mf);

                ShowWhatDialog swd = new ShowWhatDialog();

                result = swd.showDialog();

                if (result) {
                    cols = swd.getSelectedColumns();
                    lname = swd.getSelectedLocale();
                    new ChromeView(cols, lname, ph);
                }
            }
        }
    }
}
