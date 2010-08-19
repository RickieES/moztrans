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

package org.mozillatranslator.io;

import java.io.File;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  Henrik Lynggaard
 */
public class ImportTranslationDataObject implements FileDataObject {
    private String localization;
    private String fileName;
    private File realFile = null;
    private TreeNode node;

    /** Creates a new instance of ImportTranslationDataObject */
    public ImportTranslationDataObject() {
    }

    /** Getter for property localization.
     * @return Value of property localization.
     */
    public String getLocalization() {
        return this.localization;
    }

    /** Setter for property localization.
     * @param localization New value of property localization.
     */
    public void setLocalization(String localization) {
        this.localization = localization;
    }

    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public String getFileName() {
        return this.fileName;
    }

    /** Setter for property fileName.
     * @param fileName New value of property fileName.
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

    /** Getter for property node.
     * @return Value of property node.
     */
    public TreeNode getNode() {
        return this.node;
    }

    /** Setter for property node.
     * @param node New value of property node.
     */
    public void setNode(TreeNode node) {
        this.node = node;
    }
}
