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

import org.mozillatranslator.action.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author  henrik
 */
public class RedundantViewToolbar implements MozFrameToolbar {
    /** Creates a new instance of RedundantViewToolbar */
    public RedundantViewToolbar() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public JToolBar getToolbar() {
        JToolBar jtool = new JToolBar();

        jtool.add(new EditPhraseAction());
        jtool.add(new BinaryEditPhraseAction());
        jtool.add(new CopyToAllInRedundantView());
        jtool.add(new RowBatchAction(FieldToChange.FUZZY, "true"));
        jtool.add(new RowBatchAction(FieldToChange.FUZZY, "false"));
        jtool.add(new RowBatchAction(FieldToChange.KEEPORIG, "true"));
        jtool.add(new RowBatchAction(FieldToChange.KEEPORIG, "false"));
        jtool.add(new RowBatchAction(FieldToChange.TRANSLATION, ""));
        jtool.add(new RowBatchAction(FieldToChange.MULTIPLE, ""));

        jtool.setFloatable(false);
        jtool.setRollover(true);
        for (int i = 0; i < jtool.getComponentCount(); i++) {
            Component currentComponent = jtool.getComponent(i);
            currentComponent.setFont(new java.awt.Font("Dialog", 0, 12));
        }
        return jtool;
    }

    @Override
    public void init(MozFrame frame) {
    }
}
