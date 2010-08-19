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

package org.mozillatranslator.gui.dialog;

import org.mozillatranslator.kernel.*;

/**
 * Generic structure for MozillaTranslator dialogs, with methods to
 * initialize, populate the dialog with data and collect data from the
 * form
 * @author  Henrik Lynggaard
 */
public abstract class MozDialog extends javax.swing.JDialog {
    private boolean initialized;
    /**
     * Generic holder of data structures for specific dialogs
     */
    protected DataObject data;
    
    /**
     * Marks whether the dialog has closed with OK or Cancel
     * Protected so classes extending this one can alter the value
     * based on checks
     */
    protected boolean okay;

    /**
     * Method to initialize the dialog (both GUI and data)
     */
    protected abstract void init();
    
    /**
     * Transfer data from whatever-it-comes-from to the GUI
     */
    protected abstract void populate();
    
    /**
     * Transfer data from the GUI to whatever-it-must-go
     */
    protected abstract void collect();
    
    /**
     * Default constructor
     */
    public MozDialog() {
        super(Kernel.mainWindow, true);
    }
    
    /**
     * MozDialog constructor
     * @param title Dialog title
     */
    public MozDialog(String title) {
        super(Kernel.mainWindow, title, true);
    }

    /**
     * Shows the dialog (initializing it if needed)
     * @return true if the user accepted the dialog (usually with OK),
     * false otherwise
     */
    public boolean showDialog() {
        if (!initialized) {
            init();
            initialized = true;
        }
        populate();
        okay = false;
        setVisible(true);
        if (okay) {
            collect();
        }
        return okay;
    }

    /**
     * Getter for DataObject
     * @return the DataObject instance
     */
    public DataObject getDataObject() {
        return data;
    }
}
