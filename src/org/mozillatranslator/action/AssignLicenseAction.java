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

import java.io.*;
import java.util.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;


import org.mozillatranslator.filter.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.gui.*;
import org.mozillatranslator.gui.model.*;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.datamodel.*;

/** This actions displays an about mozillatranslator dialog box.
 * @version 4.15
 */
public class AssignLicenseAction extends AbstractAction {
    /** Creates new AboutAction */
    public AssignLicenseAction() {
        super(Kernel.translate("menu.advanced.assign_license.label"), null);
    }
    
    /** This get called when the action is activated.
     * @param evt The event object
     */
    public void actionPerformed(ActionEvent evt) {
        List collectedList;
        String localeName;
        List cols;
        
        cols = new ArrayList();
        cols.add(new ProductColumn());
        cols.add(new FileColumn());
        cols.add(new FileLicenseColumn());
        
        Filter filter = new LicenseFilter();
        collectedList  = FilterRunner.filterDatamodel(filter);
        Collections.sort(collectedList);
        new ComplexTableWindow("Assign license", collectedList, cols, Kernel.ORIGINAL_L10N, null);
    }
}
