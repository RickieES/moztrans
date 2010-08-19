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

package org.mozillatranslator.runner;

import java.io.File;
import org.mozillatranslator.io.FileDataObject;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author  henrik
 */
public class LoadGlossaryDataObject implements FileDataObject {
    /** Holds value of property filename. */
    private String fileName;
    private File realFile = null;

    /* Default constructor */
    public LoadGlossaryDataObject() {
    }

    /** Getter for property filename.
     * @return Value of property filename.
     *
     */
    public String getFileName() {
        return this.fileName;
    }

    /** Setter for property filename.
     * @param filename New value of property filename.
     *
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getRealFile() {
        if ((this.getFileName() != null) && (this.realFile == null)) {
            realFile = new File(this.getFileName());
        }
        return realFile;
    }
}
