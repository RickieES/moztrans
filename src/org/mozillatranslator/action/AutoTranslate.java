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
import java.util.logging.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.filter.*;
import org.mozillatranslator.gui.ComplexTableWindow;
import org.mozillatranslator.kernel.*;

/** This action tries to auto-fill empty translations with other existent
 * translations for the same original string
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class AutoTranslate extends AbstractAction {
    private static final Logger fLogger = Logger.getLogger(AutoTranslate.class.getPackage().
            getName());

    /** Creates new NoopAction
     * @param text The text to appear in the menu
     */
    public AutoTranslate() {
        super(Kernel.translate("menu.edit.auto_translate.label"));
    }

    protected org.mozillatranslator.filter.Filter getFilter(String localeName) {
        return new AllForAutoTranslate(localeName);
    }

    // TODO Improve auto-translate key-bindings
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<Phrase> collectedList;
        String localeName;
        ShowWhatDialog swd;
        List cols;

        swd = new ShowWhatDialog();
        if (swd.showDialog()) {
            localeName = swd.getSelectedLocale();
            cols = swd.getSelectedColumns();

            org.mozillatranslator.filter.Filter filter = getFilter(localeName);
            collectedList = FilterRunner.filterDatamodel(filter);
            Collections.sort(collectedList);

            Kernel.ts.translatePhraseList(collectedList, localeName);

            new ComplexTableWindow(Kernel.translate("changed_strings"),
                                    collectedList, cols, localeName, null);
        }
    }
}
