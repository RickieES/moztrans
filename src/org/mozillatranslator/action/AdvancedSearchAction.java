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

import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.filter.*;
import org.mozillatranslator.kernel.*;

/**
 * Performs an advanced search
 * @author  Henrik
 * @version 1.0
 */
public class AdvancedSearchAction extends AbstractAction {
    private int rule;
    private int column;
    private String rul;
    private String col;
    private boolean cc;
    
    /** Creates new SearchViewAction */
    public AdvancedSearchAction() {
        super(Kernel.translate("menu.edit.advanced_search.label"), null);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List collectedList;
        AdvancedSearchDialog sd = new AdvancedSearchDialog();
        
        if (sd.showDialog()) {
            AdvancedSearch search = (AdvancedSearch) sd.getDataObject();
            ShowWhatDialog swd = new ShowWhatDialog();
            swd.disableLocaleField();
            if (swd.showDialog()) {
                String localeName = search.getLocaleName();
                List cols = swd.getSelectedColumns();
                collectedList = FilterRunner.filterDatamodel(search);
                Collections.sort(collectedList);
                
                new ComplexTableWindow("Found Strings", collectedList, cols,
                                       localeName, null);
            }
        }
    }
}