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
package org.mozillatranslator.kernel;

import org.mozillatranslator.dataobjects.DataObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  henrik
 */
public abstract class MozTask implements Runnable {
    protected List affectedList;
    protected DataObject dataObject;
    protected boolean successfull;
    
    public abstract String getTitle();
    
    public List getAffectedList() {
        return affectedList;
    }
    
    public boolean wasSuccessfull() {
        return successfull;
    }
    
    public void runTask(DataObject dao) {
        dataObject = dao;
        affectedList = new ArrayList();
        Kernel.feedback.runTask(this);
    }
    
    @Override
    public void run() {
        try {
            taskImplementation();
            successfull = true;
            Kernel.feedback.endTask();
        } catch (MozException e) {
            successfull = false;
            Kernel.feedback.abortTask(e);
        }
    }
    
    public abstract void taskImplementation() throws MozException;
}
