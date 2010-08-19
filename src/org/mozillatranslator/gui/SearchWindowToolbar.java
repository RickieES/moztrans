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

package org.mozillatranslator.gui;

import java.awt.Component;
import javax.swing.JToolBar;
import org.mozillatranslator.action.BinaryEditPhraseAction;
import org.mozillatranslator.action.EditPhraseAction;
import org.mozillatranslator.action.RowBatchAction;
import org.mozillatranslator.action.SearchChromeViewAction;



/**
 * Toolbar for search results window
 * @author  uranium
 */
public class SearchWindowToolbar implements MozFrameToolbar {
    /** Creates a new instance of RedundantViewToolbar */
    public SearchWindowToolbar() {
    }

    public void destroy() {
    }

    public JToolBar getToolbar() {
        JToolBar jtool = new JToolBar();

        jtool.add(new EditPhraseAction());
        jtool.add(new BinaryEditPhraseAction());
        jtool.add(new SearchChromeViewAction());
        jtool.add(new RowBatchAction(RowBatchAction.FLD_FUZZY_ON));
        jtool.add(new RowBatchAction(RowBatchAction.FLD_FUZZY_OFF));
        jtool.add(new RowBatchAction(RowBatchAction.FLD_KEEP_ON));
        jtool.add(new RowBatchAction(RowBatchAction.FLD_KEEP_OFF));
        jtool.add(new RowBatchAction(RowBatchAction.FLD_TRNS_CLEAR));
        jtool.setFloatable(false);
        jtool.setRollover(true);
        for (int i = 0; i < jtool.getComponentCount(); i++) {
            Component currentComponent = jtool.getComponent(i);
            currentComponent.setFont(new java.awt.Font("Dialog", 0, 12));
        }
        return jtool;
    }

    public void init(MozFrame frame) {
    }
}
