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
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.filter.FilterRunner;
import org.mozillatranslator.filter.Search;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.gui.dialog.SearchDialog;
import org.mozillatranslator.gui.dialog.ShowWhatDialog;
import org.mozillatranslator.kernel.Kernel;

/** Search action tying the UI to search and the actual search operation
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class SearchAction extends AbstractAction {
    /** Creates new SearchAction
     */
    public SearchAction() {
        super(Kernel.translate("menu.edit.search.label"), null);
    }

    /** Called when the action is triggered
     * @param evt The action event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<Phrase> collectedList;

        SearchDialog sd = new SearchDialog();
        if (sd.showDialog()) {
            Search search = (Search) sd.getDataObject();
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
