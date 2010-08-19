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

import javax.swing.*;

/**
 *
 * @author  Henrik Lynggaard
 */
public class Status {
    private JProgressBar statusAndProgress;
    /** Creates a new instance of Status */
    public Status(JProgressBar bar) {
        statusAndProgress = bar;
    }
    
    public void beginWork(String work) {
        statusAndProgress.setString(work);
        statusAndProgress.setIndeterminate(true);
    }
    
    public void finishWork() {
        statusAndProgress.setValue(0);
        statusAndProgress.setMaximum(100);
        statusAndProgress.setMinimum(0);
        statusAndProgress.setString("Ready");
        statusAndProgress.setIndeterminate(false);
        //statusAndProgress.setIndeterminate(false);
    }
    
    public void setState(String display, int state) {
        statusAndProgress.setValue(state);
        statusAndProgress.setString(display);
    }
    
    public void setLength(int max) {
        statusAndProgress.setMaximum(max);
        statusAndProgress.setIndeterminate(false);
    }
    
    public void setDisplay(String display) {
        statusAndProgress.setString(display);
    }
}
